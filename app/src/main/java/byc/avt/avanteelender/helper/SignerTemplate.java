package byc.avt.avanteelender.helper;

public class SignerTemplate {

    public SignerTemplate(){}

    public static String inFramePrivyId(String privyId, String docToken, String title, String page){
        String hsl = "";

        hsl =   "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'> <meta http-equiv='X-UA-Compatible' content='ie=edge'> " +
                "<title>"+title+"</title>" +
                "</head>" +
                "<body>" +
                "<div class='privy-document'></div>" +
                "<script src='https://unpkg.com/privy-sdk'></script>" +
                "<script> Privy.openDoc('"+docToken+"', {" +
                "dev: true," +
                "container: '.privy-document'," +
                "privyId : '"+privyId+"'," +
                "signature: {" +
                "page: "+page+"," +
                "x: 130," +
                "y: 468," +
                "fixed: false" +
                "}" +
                "}).on('after-sign', (data) => {" +
                "location.href = ''" +
                "})" +
                "</script>" +
                "</body>" +
                "</html>";

        return hsl;
    }


}
