import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * 因为氛围灯时序参数是一个excel 表格，要生成对应的xml文件以供代码解析
 * 本类是一个工具类，读取xlsx表格文件，生成对应的xml文件
 * 注意
 * 1.模板sheet放在index==1的地方(也就是第2个)
 * 2.视情况修改 inputPath，和 outPutPath，colTotals
 * 3.输出格式如下
 * <xml>
 * <Node color="19" bright="0"/>
 * ...
 * <Node color="30" bright="10"/>
 * </xml>
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        final String inputPath = "src/main/resources/sober.xlsx";
        InputStream inputStream = new FileInputStream(inputPath);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheetAt = workbook.getSheetAt(1);
        // 读取控制节点排
        Row row0 = sheetAt.getRow(0);
        // 读取颜色排
        Row row1 = sheetAt.getRow(1);
        // 读取亮度排
        Row row2 = sheetAt.getRow(2);

        Document doc = DocumentHelper.createDocument();
        //添加xml根节点
        Element xml = doc.addElement("xml");
        // 总列数
        int colTotals = 2577;
        for (int i = 0; i < colTotals; i++) {
            double color = row1.getCell(i).getNumericCellValue();
            double bright = row2.getCell(i).getNumericCellValue();
            // 循环创建Node 节点并完成属性添加
            xml.addElement("Node")
                    .addAttribute("color", String.valueOf((int) Math.floor(color)))
                    .addAttribute("bright", String.valueOf((int) Math.floor(bright)));
            System.out.println(color);
            System.out.println(bright);

            System.out.println("----------------");
        }
        //  格式化文件
        OutputFormat format = new OutputFormat();
        // 行缩进
        format.setIndentSize(2);
        // 一个结点为一行
        format.setNewlines(true);
        // 去重空格
        format.setTrimText(true);
        format.setPadText(true);
        // 放置xml文件中第二行为空白行
        format.setNewLineAfterDeclaration(false);
        // 输入文件路径
        String outPutPath = "src/main/resources/sober.xml";
        XMLWriter writer = new XMLWriter(new FileOutputStream(outPutPath), format);
        //完成写入
        writer.write(doc);
    }
}
