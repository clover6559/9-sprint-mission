import menu.UserMenu;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;
import service.jcf.JCFUserService;

import java.util.Scanner;

public class StreamMain {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(userService, channelService);
        UserMenu userMenu = new UserMenu();
        Scanner main = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.println("\\n=== 항목 === ");
            System.out.println("1. User ");
            System.out.println("2. Channel ");
            System.out.println("3. Message ");
            System.out.println("4. 종료 ");
            System.out.println("선택 :  ");
            int menu = main.nextInt();
            main.nextLine();

            switch (menu) {
                case 1:
                    void runUserMenu();
//



                        break;
                    }
            } main.close();
        }



    }
}
