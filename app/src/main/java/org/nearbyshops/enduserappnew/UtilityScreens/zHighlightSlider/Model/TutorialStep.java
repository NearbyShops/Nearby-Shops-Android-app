package org.nearbyshops.enduserappnew.UtilityScreens.zHighlightSlider.Model;



import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;

import java.util.ArrayList;
import java.util.List;

public class TutorialStep {



    private String titleTop;
    private String title;
    private String description;

    private int actionCode;


    public static int ACTION_CODE_WHATSAPP_BOOKING_HELPLINE = 3;



    // highlight builders


    public static Highlights getCabBookingTutorial()
    {

        TutorialStep step_one = new TutorialStep();
        step_one.setTitleTop("Step 1 : Send Request");
        step_one.setTitle("Send Request to One or Many Drivers");
        step_one.setDescription("Dont worry request is just the suggestion. Driver wont come until you confirm.");


        TutorialStep step_two = new TutorialStep();
        step_two.setTitleTop("Step 2 : Select Driver");
        step_two.setTitle("Select anyone from willing Drivers");
        step_two.setDescription("Select any driver you like who are willing to go with you.");





        TutorialStep step_three = new TutorialStep();
        step_three.setTitleTop("Call Driver if Required !");
        step_three.setTitle("Call the driver if needed");
        step_three.setDescription("If a driver does not respond in 2 minutes you can call the driver. Use the phone number provided.");





        TutorialStep step_four = new TutorialStep();
        step_four.setActionCode(ACTION_CODE_WHATSAPP_BOOKING_HELPLINE);
        step_four.setTitleTop("Still Facing Difficulty ?");
        step_four.setTitle("Message us on our Whatsapp Booking Helpline");
        step_four.setDescription("Send us your pickup and destination and we will arrange a cab for you.");




        List<Object> list = new ArrayList<>();
        list.add(step_one);
        list.add(step_two);
        list.add(step_three);
        list.add(step_four);




        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);
        highlights.setListTitle("How to Book a Cab ?");


        return highlights;
    }





    public static Highlights getReferralTutorial()
    {



        TutorialStep step_zero = new TutorialStep();
        step_zero.setTitleTop("Refer Friends and Earn Cash");

        step_zero.setTitle(
                PrefGeneral.getCurrencySymbol(MyApplication.getAppContext())
                + " 15 after Sign Up and "
                + PrefGeneral.getCurrencySymbol(MyApplication.getAppContext())
                +" 35 after First trip"
        );




        step_zero.setDescription("Earn 15 Rupees after your friend sign up and 35 rupees after your friend makes first trip.");


        TutorialStep step_one = new TutorialStep();
        step_one.setTitleTop("Step 1 : Sign Up");
        step_one.setTitle("Tell your friend to sign-up using your referral code");
        step_one.setDescription("Your referral code is provided in profile section.");


        TutorialStep step_two = new TutorialStep();
        step_two.setTitleTop("Step 2 : Check Referral Tracker");
        step_two.setTitle("Check your Referral Tracker");
        step_two.setDescription("You will see the name of your friend. Referral is Successful.");



        TutorialStep step_three = new TutorialStep();
        step_three.setTitleTop("Step 3 : Check Wallet");
        step_three.setTitle("Check the wallet credit");
        step_three.setDescription("Click on the wallet credit you will see 15 Rupees deposited immediately after sign up and 35 rupees will be deposited after first trip.");




        TutorialStep step_four = new TutorialStep();
        step_four.setTitleTop("Step 4 : Withdraw credit");
        step_four.setTitle("Withdraw your wallet credit");
        step_four.setDescription("Call customer care to withdraw your credit it will be deposited into your UPI account associated with your phone number.");




        List<Object> list = new ArrayList<>();
        list.add(step_zero);
        list.add(step_one);
        list.add(step_two);
        list.add(step_three);
        list.add(step_four);




        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);
        highlights.setListTitle("Refer Friends and Earn Cash !");


        return highlights;
    }





    // Getter and Setters


    public String getTitleTop() {
        return titleTop;
    }

    public void setTitleTop(String titleTop) {
        this.titleTop = titleTop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }
}
