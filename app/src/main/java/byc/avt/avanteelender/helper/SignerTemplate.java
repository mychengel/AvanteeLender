package byc.avt.avanteelender.helper;

public class SignerTemplate {

    public SignerTemplate(){}

    public static String inFramePrivyId(String privyId, String docToken){
        String hsl = "";

        hsl = hsl + "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'> <meta http-equiv='X-UA-Compatible' content='ie=edge'> <title>Document</title>" +
                "</head>" +
                "<body>" +
                "<div class='privy-document'></div>" +
                "<script src='https://unpkg.com/privy-sdk@next'></script>" +
                "<script> Privy.openDoc('"+docToken+"', {" +
                "dev: true," +
                "container: '.privy-document'," +
                "privyId : '"+privyId+"'," +
                "//Autofill privyID" +
                "signature: {" +
                "page: 11," +
                "x: 130," +
                "y: 468," +
                "fixed: false" +
                "}" +
                "}).on('after-action', (data) => {" +
                "location.href = '' //After sign/review doc" +
                "})" +
                ".on('after-sign', (data) => { //After sign doc" +
                "location.href = ''" +
                "})" +
                ".on('after-review', (data) => { //After review doc" +
                "location.href = ''" +
                "})" +
                "</script>" +
                "</body>" +
                "</html>";

        return hsl;
    }


}
