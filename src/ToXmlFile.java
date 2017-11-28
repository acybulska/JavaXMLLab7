import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ToXmlFile {
    private Document doc;
    Element results;

    ToXmlFile()
    {
        OpenFile();
    }
    public void OpenFile()
    {
        try{
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.newDocument();

            results = doc.createElement("Results");
            doc.appendChild(results);

        } catch (Exception e) {
        System.out.println(e);
        }
    }

    public void AverageToFile(List<ResponsePeriod> list, String id) {
        try {

            Element averages = doc.createElement("Averages");
            averages.setAttribute("Id", id);
            results.appendChild(averages);

            for(int i=0;i<list.size();i++)
            {
                double av = list.get(i).GetSum()/list.get(i).GetCounter();
                Element average = doc.createElement(list.get(i).GetName());
                average.setNodeValue(String.valueOf(av));
                averages.appendChild(average);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void PercentageToFile (List<ResponsePeriod> okList, List<ResponsePeriod> falseList) {
        try {

            Element percentage = doc.createElement("Percentage");
            percentage.setAttribute("Id", "false");
            results.appendChild(percentage);

            for(int i=0;i<falseList.size();i++)
            {
                ResponsePeriod res=falseList.get(i);
                List<ResponsePeriod> correct_acts = okList.stream().filter(o -> o.GetName().equalsIgnoreCase(res.GetName())).collect(Collectors.toList());
                Element percent = doc.createElement(falseList.get(i).GetName());
                double per = (double)falseList.get(i).GetCounter() * 100 / (double)(correct_acts.get(0).GetCounter() + falseList.get(i).GetCounter());
                percent.setNodeValue(per+"%");
                percentage.appendChild(percent);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void CloseFile()
    {
        try{
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            StreamResult tofile = new StreamResult(new File("C:\\Users\\Lisa\\Documents\\Java Projects\\Lab7\\src\\file1.xml"));

            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            String xmlString = sw.toString();
            //print xml
            System.out.println("Here's the xml:\n\n" + xmlString);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, tofile);


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
