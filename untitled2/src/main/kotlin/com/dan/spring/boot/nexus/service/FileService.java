package com.dan.spring.boot.nexus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dan.spring.boot.nexus.handler.FileHandler;
import com.dan.spring.boot.nexus.handler.NexusComponentQuery;
import com.dan.spring.boot.nexus.handler.NexusComponentUploader;
import com.dan.spring.boot.nexus.pojo.NexusPojo;
import com.dan.spring.boot.nexus.pojo.UploadItem;
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
import java.util.concurrent.TimeUnit;

@Service
public class FileService {
    @Autowired
    private FileHandler mFileHandler;
    private CacheHolder<List<UploadItem>> localFileCache;
    private CacheHolder<List<NexusPojo>> remoteNexusCache;

    @PostConstruct
    public void init() {
        localFileCache = new CacheHolder<List<UploadItem>>(20, TimeUnit.SECONDS) {
            @Override
            public List<UploadItem> refresh() {
                mFileHandler.setNexusPojoList(remoteNexusCache.get());
                return mFileHandler.getAllFile();
            }
        };

        remoteNexusCache = new CacheHolder<List<NexusPojo>>() {
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
        List<NexusPojo> list = new ArrayList<>();
        String token = "";
        do {
            String result = NexusComponentQuery.Constant.query(token);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray items = jsonObject.getJSONArray("items");
            token = jsonObject.getString("continuationToken");
            List<NexusPojo> temp = JSONArray.parseArray(items.toJSONString(), NexusPojo.class);
            list.addAll(temp);
        } while (!StringUtils.isEmpty(token));
        return list;
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

    @PreDestroy
    public void down() {
        localFileCache.destroy();
        remoteNexusCache.destroy();
    }
}
