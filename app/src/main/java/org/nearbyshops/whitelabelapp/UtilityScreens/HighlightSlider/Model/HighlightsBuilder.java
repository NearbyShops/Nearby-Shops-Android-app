package org.nearbyshops.whitelabelapp.UtilityScreens.HighlightSlider.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;



public class HighlightsBuilder {


    public static HighlightListItem getHighlightOne(Context context)
    {
        return new HighlightListItem(
                "https://images.unsplash.com/photo-1592502712628-c5219bf0bc12?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=501&q=80"
        );
    }



    public static HighlightListItem getHighlightTwo(Context context)
    {
        return new HighlightListItem("https://images.unsplash.com/photo-1488459716781-31db52582fe9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=300&q=80");
    }


    public static HighlightListItem getHighlightThree(Context context)
    {
        return new HighlightListItem("https://images.unsplash.com/photo-1542838132-92c53300491e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=507&q=80");
    }




    public static HighlightListItem getCreateShopOne(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/hyEsrD7.png",1
        );
    }



    public static HighlightListItem getCreateShopTwo(Context context)
    {
        return new HighlightListItem("https://i.imgur.com/tr87TfC.png",2);
    }


    public static HighlightListItem getCreateShopThree(Context context)
    {
        return new HighlightListItem("https://i.imgur.com/JxXQlTC.png",3);
    }


    public static HighlightListItem getCreateShopFour(Context context)
    {
        return new HighlightListItem("https://i.imgur.com/iquI5qA.png");
    }




    public static HighlightList getHighlightsCreateShop(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();

        list.add(getCreateShopOne(context));
        list.add(getCreateShopTwo(context));
        list.add(getCreateShopThree(context));
//        list.add(getCreateShopFour(context));

        HighlightList highlightList = new HighlightList();
        highlightList.setHighlightItemList(list);
        highlightList.setListTitle("Steps For Launch");
        highlightList.setShowTutorial(true);


        return highlightList;
    }




    public static HighlightListItem getCreateMarketOne(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/ZXmpVHk.png",1
        );
    }


    public static HighlightListItem getCreateMarketTwo(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/61zMheu.png",2
        );
    }


    public static HighlightListItem getCreateMarketThree(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/PSwJNXs.png",3
        );
    }






    public static HighlightList getHighlightsCreateMarket(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();

        list.add(getCreateMarketOne(context));
        list.add(getCreateMarketTwo(context));
        list.add(getCreateMarketThree(context));

        HighlightList highlightList = new HighlightList();
        highlightList.setHighlightItemList(list);
        highlightList.setListTitle("Steps For Launch");
        highlightList.setShowTutorial(true);


        return highlightList;
    }









    public static HighlightListItem getCreateMarketOneSM(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/ZXmpVHk.png",1
        );
    }


    public static HighlightListItem getCreateMarketTwoSM(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/61zMheu.png",2
        );
    }


    public static HighlightListItem getCreateMarketThreeSM(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/PSwJNXs.png",3
        );
    }





    public static HighlightList getHighlightsSetupMarketSM(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();

        list.add(getCreateMarketOneSM(context));
        list.add(getCreateMarketTwoSM(context));
        list.add(getCreateMarketThreeSM(context));

        HighlightList highlightList = new HighlightList();
        highlightList.setHighlightItemList(list);
        highlightList.setListTitle("Steps For Launch");
        highlightList.setShowTutorial(true);


        return highlightList;
    }





    public static HighlightListItem getAddShopOne(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/yBlO2UQ.png",1
        );
    }


    public static HighlightListItem getAddShopTwo(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/nnmlB91.png",2
        );
    }


    public static HighlightListItem getAddShopThree(Context context)
    {
        return new HighlightListItem(
                "https://i.imgur.com/vmPnstt.png",3
        );
    }





    public static HighlightList getHighlightsAddShopToMarket(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();

        list.add(getAddShopOne(context));
        list.add(getAddShopTwo(context));
        list.add(getAddShopThree(context));

        HighlightList highlightList = new HighlightList();
        highlightList.setHighlightItemList(list);
        highlightList.setListTitle("How to Add a Shop ?");
        highlightList.setShowTutorial(true);


        return highlightList;
    }





    public static HighlightList getHighlightsFrontScreen(Context context)
    {

        if(context==null)
        {
            return null;
        }


        List<Object> list = new ArrayList<>();

        list.add(getHighlightOne(context));
        list.add(getHighlightTwo(context));
        list.add(getHighlightThree(context));

        HighlightList highlightList = new HighlightList();
        highlightList.setHighlightItemList(list);
        highlightList.setListTitle("Steps To Launch");
        highlightList.setShowTutorial(true);


        return highlightList;
    }



}
