import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ex1 {

    public void StartEx()
    {
        SAXmethod();
    }

    private void SAXmethod() {
        try {
            File inputFile = new File("C:\\Users\\Lisa\\Documents\\Java Projects\\Lab7\\src\\simres59444.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            UserHandler userhandler = new UserHandler();
            saxParser.parse(inputFile, userhandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UserHandler extends DefaultHandler {
        List<ResponsePeriod> responsePeriodReq = new ArrayList<>();
        List<ResponsePeriod> responsePeriodAct = new ArrayList<>();
        List<ResponsePeriod> responsePeriodFalse = new ArrayList<>();

        ToXmlFile toFile = new ToXmlFile();

        @Override
        public void startElement(
                String uri, String localName, String qName, Attributes attributes)
                throws SAXException {

            if (qName.equalsIgnoreCase("measurement")) {
                if (attributes.getValue("status").equalsIgnoreCase("ok")) {
                    if(attributes.getValue("id").equalsIgnoreCase("req"))
                    {
                        int i;
                        boolean isValue=false;
                        for(i=0;i<responsePeriodReq.size();i++)
                        {
                            if(responsePeriodReq.get(i).serviceComponentName.equalsIgnoreCase(attributes.getValue("servicecomponent_name")))
                            {
                                isValue=true;
                                break;
                            }
                        }

                        if(isValue)
                        {
                            responsePeriodReq.get(i).SetSum(responsePeriodReq.get(i).GetSum()+Double.parseDouble(attributes.getValue("responseperiod")));
                            responsePeriodReq.get(i).SetCounter(responsePeriodReq.get(i).GetCounter()+1);
                        }
                        else
                        {
                            ResponsePeriod newResponse=new ResponsePeriod();
                            newResponse.SetName(attributes.getValue("servicecomponent_name"));
                            newResponse.SetSum(Double.parseDouble(attributes.getValue("responseperiod")));
                            newResponse.SetCounter(newResponse.GetCounter()+1);
                            responsePeriodReq.add(newResponse);
                        }
                    }
                    if(attributes.getValue("id").equalsIgnoreCase("act"))
                    {
                        int i;
                        boolean isValue=false;
                        for(i=0;i<responsePeriodAct.size();i++)
                        {
                            if(responsePeriodAct.get(i).serviceComponentName.equalsIgnoreCase(attributes.getValue("activityname")))
                            {
                                isValue=true;
                                break;
                            }
                        }

                        if(isValue)
                        {
                            responsePeriodAct.get(i).SetSum(responsePeriodAct.get(i).GetSum()+Double.parseDouble(attributes.getValue("responseperiod")));
                            responsePeriodAct.get(i).SetCounter(responsePeriodAct.get(i).GetCounter()+1);
                        }
                        else
                        {
                            ResponsePeriod newResponse=new ResponsePeriod();
                            newResponse.SetName(attributes.getValue("activityname"));
                            newResponse.SetSum(Double.parseDouble(attributes.getValue("responseperiod")));
                            newResponse.SetCounter(newResponse.GetCounter()+1);
                            responsePeriodAct.add(newResponse);
                        }
                    }
                }
                if(attributes.getValue("status").equalsIgnoreCase("false") && attributes.getValue("id").equalsIgnoreCase("act"))
                {
                    int i;
                    boolean isValue=false;
                    for(i=0;i<responsePeriodFalse.size();i++)
                    {
                        if(responsePeriodFalse.get(i).serviceComponentName.equalsIgnoreCase(attributes.getValue("activityname")))
                        {
                            isValue=true;
                            break;
                        }
                    }

                    if(isValue)
                    {
                        responsePeriodFalse.get(i).SetCounter(responsePeriodFalse.get(i).GetCounter()+1);
                    }
                    else
                    {
                        ResponsePeriod newResponse=new ResponsePeriod();
                        newResponse.SetName(attributes.getValue("activityname"));
                        newResponse.SetCounter(newResponse.GetCounter()+1);
                        responsePeriodFalse.add(newResponse);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri,
                               String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("simulation")) {
                toFile.AverageToFile(responsePeriodReq,"res");
                toFile.AverageToFile(responsePeriodAct,"act");
                toFile.PercentageToFile(responsePeriodAct,responsePeriodFalse);
                toFile.CloseFile();
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
        }
    }
}
