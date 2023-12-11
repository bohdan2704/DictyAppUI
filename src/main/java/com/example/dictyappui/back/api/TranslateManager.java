//package com.example.dictyappui.back.api;
//
//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;
//import com.example.dictyappui.back.db.DatabaseConnection;
//import com.example.dictyappui.back.redundant.FileReader;
//
//import java.util.List;
//
//public class TranslateManager {
//    private static final String fromLang = "en";
//    private static final String toLang = "uk";
//    // Using google CLI and ADC to connect to server
//    private final DatabaseConnection dbConn;
//    private final Translate translate = TranslateOptions.getDefaultInstance().getService();
//
//    public TranslateManager(DatabaseConnection dbConn) {
//        this.dbConn = dbConn;
//    }
//
//    public String toUkranian(String enWord) {
//        Translation translation = translate.translate(
//                enWord,
//                Translate.TranslateOption.sourceLanguage(fromLang),
//                Translate.TranslateOption.targetLanguage(toLang));
//
//        return translation.getTranslatedText();
//    }
//
//    public void writeWordAndTranslation() {
//        FileReader fileReader = new FileReader();
//        List<String> enWordsList = fileReader.readStringsFromFile();
//        for (String enWord : enWordsList) {
//            String ukWord = toUkranian(enWord);
//            int i = dbConn.getDbSize();
//            // DO SOMETHING WITH TRANSLATED WORD
//        }
//        System.out.println("Success, check table!");
//    }
//}
//// Dumb realization using direct API key, google recommends using CLI (should be installed)
//// to create ADC (Application default credentials) though authorization to google account via browser
//
////        String apiKey = "AIzaSyAcx0Hm9DaxkUJiPlnHAA9RcVboSc2B0eA";
////        Translate translate = TranslateOptions.newBuilder()
////                .setApiKey(apiKey)
////                .build()
////                .getService();
