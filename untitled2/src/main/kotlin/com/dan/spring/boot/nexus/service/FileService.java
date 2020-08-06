package com.dan.spring.boot.nexus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dan.spring.boot.nexus.handler.FileHandler;
import com.dan.spring.boot.nexus.handler.NexusComponentQuery;
import com.dan.spring.boot.nexus.handler.NexusComponentUploader;
import com.dan.spring.boot.nexus.pojo.NexusPojo;
import com.dan.spring.boot.nexus.pojo.UploadItem;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {
    @Autowired
    private FileHandler mFileHandler;
    private CacheHolder<List<UploadItem>> localFileCache;
    private CacheHolder<List<NexusPojo>> remoteNexusCache;

    @PostConstruct
    public void init() {
        localFileCache = new CacheHolder<List<UploadItem>>(20000, TimeUnit.SECONDS) {
            @Override
            public List<UploadItem> refresh() {
                mFileHandler.setNexusPojoList(remoteNexusCache.get());
                return mFileHandler.getAllFile();
            }
        };

        remoteNexusCache = new CacheHolder<List<NexusPojo>>(20000, TimeUnit.SECONDS) {
            @Override
            public List<NexusPojo> refresh() {
                return queryNexus();
            }
        };
    }

    public List<UploadItem> findAll() {
        return localFileCache.get();
    }

    public List<NexusPojo> queryNexus() {
        final List<NexusPojo>[] result = new List[]{new ArrayList<>()};
        Observable.zip(NexusComponentQuery.Constant.getSnapshot(), NexusComponentQuery.Constant.getRelease(),
                new BiFunction<List<NexusPojo>, List<NexusPojo>, List<NexusPojo>>() {
                    @Override
                    public List<NexusPojo> apply(List<NexusPojo> nexusPojos, List<NexusPojo> nexusPojos2) throws Exception {
                        List<NexusPojo> list = new ArrayList<>();
                        list.addAll(nexusPojos);
                        list.addAll(nexusPojos2);
                        return list;
                    }
                }).blockingSubscribe(new Consumer<List<NexusPojo>>() {
            @Override
            public void accept(List<NexusPojo> nexusPojos) throws Exception {
                result[0] = nexusPojos;
            }
        });
        return result[0];
    }

    public void upload(UploadItem item) {
        NexusComponentUploader.Constant.upload(item, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                remoteNexusCache.fetch();
            }
        });
    }

    public void uploadSync(UploadItem item) {
        CountDownLatch latch = new CountDownLatch(1);
        NexusComponentUploader.Constant.upload(item, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                latch.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                remoteNexusCache.fetch();
                localFileCache.fetch();
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @PreDestroy
    public void down() {
        localFileCache.destroy();
        remoteNexusCache.destroy();
    }
}
