
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.mycompany.openbot.NNetwork;


public class OpenSpaceBot extends TelegramLongPollingBot {

    public NNetwork net;
    public HashMap<String,Integer> dict;
        public int input_nodes = 100;
        public int output_nodes = 900;
        public int hidden_nodes = 100;
        public int epochs = 100;
        public float learning_rate = 0.3f;
    public OpenSpaceBot() throws Exception{
        boolean scan_train = false;
        net = new com.mycompany.openbot.NNetwork(input_nodes,hidden_nodes,output_nodes,learning_rate);
        dict = new HashMap<String,Integer>();
        FileReader question_1 = new FileReader("quest.diamon");
        Scanner question_scanner_1 = new Scanner(question_1);
        int questions_count = 0;
        while(question_scanner_1.hasNextLine()){
            question_scanner_1.nextLine();
            questions_count++;
        }
        question_1.close();
        FileReader question_2 = new FileReader("quest.diamon");
        String[] questions = new String[questions_count];
        Scanner question_scanner_2 = new Scanner(question_2);
        int num=0;
        while(question_scanner_2.hasNextLine()){
            questions[num] = question_scanner_2.nextLine();
            num++;
        }
        question_2.close();
        int number = 0;
        while(scan_train){
        FileReader words_reader = new FileReader("words.diamon");
        Scanner word_scanner = new Scanner(words_reader);
        int words_num =0;
        while(word_scanner.hasNextLine()){
        String s = word_scanner.nextLine();
        String[] spl = s.split(",");
        int key = java.lang.Integer.parseInt(spl[0]);
        if(!dict.containsKey(spl[1])) dict.put(spl[1], key);
        words_num++;
        }
        String message = questions[number];
        message = message.toLowerCase();
        message = message.replaceAll("[,?.!]", "");
        String words[] = message.split(" ");
        int k=0;
        for(int i=0;i<words.length;i++){
            if(!dict.containsKey(words[i])){
                dict.put(words[i],words_num+k+1);
                k++;
            }
        }
        String data = "";
        for(Object key1:dict.keySet()){
            Object value = dict.get(key1);
            data += (value + "," + key1 + "\n");
        }
        Files.write(Paths.get("words.diamon"), data.getBytes());
        int answer = number;
        FileReader train_reader = new FileReader("train_strings.diamon");
        Scanner train_read = new Scanner(train_reader);
        data = "";
        while(train_read.hasNextLine()) 
            data += train_read.nextLine() + "\n";
        data += answer + ",";
        for(int i=0;i<words.length;i++){
            data += dict.get(words[i]);
            if(i!=words.length-1) data+=",";
        }
        Files.write(Paths.get("train_strings.diamon"), data.getBytes());
        number++;
        }
        for(int j=0;j<epochs;j++){
            FileReader train = new FileReader("train_strings.diamon");
            Scanner train_scan = new Scanner(train);
            while(train_scan.hasNextLine()){
                String line = train_scan.nextLine();
                double[] input = new double[input_nodes];
                double[] targets = new double[output_nodes];
                for(int i=0;i<targets.length;i++) targets[i] = 0.01;
                String[] nums = line.split(",");
                int tar = java.lang.Integer.parseInt(nums[0]);
                targets[tar] = 0.99;
                for(int i=0;i<input.length;i++) input[i] = 0.0;
                for(int i=1;i<nums.length-1;i++){
                    int x = java.lang.Integer.parseInt(nums[i]);
                    input[i] = x/50.0 * 0.99;
                }
                net.train(input,targets);
            }
        }
        FileReader words_reader = new FileReader("words.diamon");
        Scanner word_scanner = new Scanner(words_reader);
        while(word_scanner.hasNextLine()){
        String s = word_scanner.nextLine();
        String[] spl = s.split(",");
        int key = java.lang.Integer.parseInt(spl[0]);
        if(!dict.containsKey(spl[1])) dict.put(spl[1], key);
        }
    }
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if(msg.equals("/start")){
            sendMsg(msg,"Hello");
        }
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        text = text.replaceAll("[,.!?]","");
        String[] input_words = text.split(" ");
        double[] input = new double[input_nodes];
        for(int i=0;i<input.length;i++) input[i] = 0.01;
        for(int i=0;i<input_words.length;i++){
            if(dict.containsKey(input_words[i])) 
                input[i] = dict.get(input_words[i])/50.0*0.99;
            else input[i] = 0.01;
        }
        double[][] result = net.query(input);
        double max=0.0;
        int a = -1;
        for(int i=0;i<result.length;i++){
            if(result[i][0]> max){
                max = result[i][0];
                a = i;
            }
        }
        sendMsg(msg,"Ответ сети:" + a);


    }

    public String getBotUsername() {
        return "MyFirstBot";
    }

    public String getBotToken() {
        return "1149859035:AAEExc5GwHzQdd3ABCvp9BLpi9xth136Pg4";
    }
    private void sendMsg(Message msg, String text){
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try{
            sendMessage(s);
            
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
        
    }
}
