package xrate;

import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
* Provide access to basic currency exchange rate services.
* 
* @author TEAM
*         Francisco Montanez
*/

public class ExchangeRateReader {
    
    /**
    * Construct an exchange rate reader using the given base URL. All requests
    * will then be relative to that URL. If, for example, your source is Xavier
    * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
    * for specific days will be constructed from that URL by appending the
    * year, month, and day; the URL for 25 June 2010, for example, would be
    * http://api.finance.xaviermedia.com/api/2010/06/25.xml
    * 
    * @param baseURL
    *            the base URL for requests
    */
    
    private String baseURL;
    
    // the base
    public ExchangeRateReader(String baseURL) {
        this.baseURL = baseURL;
    }
    
    /**
    * Get the exchange rate for the specified currency against the base
    * currency (the Euro) on the specified date.
    * 
    * @param currencyCode
    *            the currency code for the desired currency
    * @param year
    *            the year as a four digit integer
    * @param month
    *            the month as an integer (1=Jan, 12=Dec)
    * @param day
    *            the day of the month as an integer
    * @return the desired exchange rate
    * @throws IOException
    * @throws ParserConfigurationException
    * @throws SAXException
    */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException, 
    ParserConfigurationException, SAXException {
        
        String cURL;
        
        // for concatenation of months and days if required
        if (month < 10) {
            cURL = baseURL + year + "/0" + month;
        }
        else {
            cURL = baseURL + year + "/" + month;
        }
        
        if (day < 10) {
            cURL = cURL + "/0" + day + ".xml";
        }
        else {
            cURL = cURL + "/" + day + ".xml";
        }
        
        URL url = new URL(cURL);
        Document doc = createDoc(url);
        
        // using tag name to get nodelist
        NodeList currencyCL = doc.getElementsByTagName("currency_code");
        
        // to find index of target in nodelist
        int index = findIndexOf(currencyCL, currencyCode);
        
        // using tag name to get another nodelist
        NodeList rList = doc.getElementsByTagName("rate");
        
        // finding exchange rate in nodelist
        float exRate = findRate(rList, index);
        
        return exRate;
    }
    
    /**
    * Get the exchange rate of the first specified currency against the second
    * on the specified date.
    * 
    * @param currencyCode
    *            the currency code for the desired currency
    * @param year
    *            the year as a four digit integer
    * @param month
    *            the month as an integer (1=Jan, 12=Dec)
    * @param day
    *            the day of the month as an integer
    * @return the desired exchange rate
    * @throws IOException
    * @throws ParserConfigurationException
    * @throws SAXException
    */
    public float getExchangeRate(
        String fromCurrency, String toCurrency,
        int year, int month, int day) throws IOException, ParserConfigurationException, SAXException {
    
    
    
    String cURL;
    
   // for concatenation of months and days if required
    
    if (month < 10) {
        
        cURL = baseURL + year + "/0" + month;
        
    } else {
        
        cURL = baseURL + year + "/" + month;
        
    }
    
    if (day < 10) {
        
        cURL = cURL + "/0" + day + ".xml";
        
    } else {
        
        cURL = cURL + "/" + day + ".xml";
        
    }
    
    
    
    URL url = new URL(cURL);
    
    Document doc = createDoc(url);
    
    
    
    NodeList currCodeList = doc.getElementsByTagName("currency_code");
    
    int indexOfFromCurrency = findIndexOf(currCodeList, fromCurrency);
    
    int indexOfToCurrency = findIndexOf(currCodeList, toCurrency);
    
    
    
    NodeList rList = doc.getElementsByTagName("rate");
    
    float rateFromCurrency = findRate(rList, indexOfFromCurrency);
    
    float rateToCurrency = findRate(rList, indexOfToCurrency);
    
    // exchange rate formula
    float exchangeRate = rateFromCurrency/rateToCurrency;
    
    
    
    return exchangeRate;
    
        }
        
        
        
        private Document createDoc(URL url) throws IOException, ParserConfigurationException, SAXException {
            
            InputStream xmlStream = url.openStream();
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(xmlStream);
            
            return doc;
            
        }
        
        
        // method to find index
        private int findIndexOf(NodeList currCodeList, String targetCurrency) {
            
            int index = 0;
            
            for (int i = 0; i < currCodeList.getLength(); i++) {

                //casting element in the code list
                Element currency = (Element)currCodeList.item(i);
                
                String currencyAsString = currency.getTextContent();
                
                if (targetCurrency.equals(currencyAsString)) {
                    
                    break;
                    
                } else {

                    //increase index
                    index++;
                    
                }
                
            }
            
            return index;
            
        }
        
        
        // method to find the rate
        private float findRate(NodeList rlist, int index) {

            //cast element in the rate list
            Element rate = (Element)rlist.item(index);
            
            String rateAsString = rate.getTextContent();
            
            float rateAsFloat = Float.parseFloat(rateAsString);
            
            return rateAsFloat;
            
        }
}

