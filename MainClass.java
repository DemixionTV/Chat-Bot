
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MainClass {
    public static void main(String[] args) throws Exception {
        
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new OpenSpaceBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

