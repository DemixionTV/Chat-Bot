import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.apache.log4j.Logger;


public class OpenSpaceBot extends TelegramLongPollingBot {


    public void onUpdateReceived(Update update) {

        String command=update.getMessage().getText();

        SendMessage message = new SendMessage();

        if(command.equals("/myname")){

            System.out.println(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName());

            message.setText(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName());
        }
        if(command.equals("/start")){

            System.out.println(update.getMessage().getFrom()+"Hi! I`m OpenSpaceBot");

            message.setText("Hi! I'm OpenSpaceBot");
        }

        message.setChatId(update.getMessage().getChatId());


        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    public String getBotUsername() {
        return "MyFirstBot";
    }

    public String getBotToken() {
        return "1279048251:AAEFTN-JUynu7s4rX6o4tHN6E89YWDYWA50";
    }
}
