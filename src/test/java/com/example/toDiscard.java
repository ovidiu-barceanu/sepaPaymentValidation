package com.example;

public class toDiscard {

    //    public void getDebtorTotalAmount(){
////        sum up all the credit transfer transactions listed under <CdtTrfTxInf>
////        compare it with the control sum under CtrlSum from PmtInf and GrpHdr
////        if equal return the sum
//
//    }

//    public static Double calculateAndCompareTotal(String xmlFilePath) {
//        try {
//            // Parse the XML document
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document document = builder.parse(new File(xmlFilePath));
//            document.getDocumentElement().normalize();
//
//            // Get control sum from the first CtrlSum element
//            String ctrlSumStr = document.getElementsByTagName("CtrlSum").item(0).getTextContent();
//            double controlSum = Double.parseDouble(ctrlSumStr);
//
//            // Sum all amounts from CdtTrfTxInf
//            NodeList cdtTrfTxInfList = document.getElementsByTagName("CdtTrfTxInf");
//            double totalAmount = 0.0;
//
//            for (int i = 0; i < cdtTrfTxInfList.getLength(); i++) {
//                // Access each CdtTrfTxInf node
//                Element cdtTrfTxInfElement = (Element) cdtTrfTxInfList.item(i);
//
//                // Get InstdAmt element safely
//                NodeList instdAmtList = cdtTrfTxInfElement.getElementsByTagName("InstdAmt");
//                if (instdAmtList.getLength() > 0) {
//                    String amountStr = instdAmtList.item(0).getTextContent();
//                    totalAmount += Double.parseDouble(amountStr);
//                }
//            }
//
//            // Compare total with control sum
//            return (totalAmount == controlSum) ? totalAmount : null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
