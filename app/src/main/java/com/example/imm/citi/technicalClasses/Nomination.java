package com.example.imm.citi.technicalClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.example.imm.citi.activities.PollActivity;

import java.util.ArrayList;

/**
 * Created by Sujoy on 4/10/2017.
 */

public class Nomination implements Parcelable{
    public String name, description, nominator, dateAdded;
    public String nominatorName, nominatorPhone, nominatorBio;
    public ArrayList<String> sources, filters, cities;
    public int voteCount;
    public Boolean canVote;

    private Activity parent;
    PollActivity pollParent;

    private final String ADDNOMFILE = "addNomination.php", UPDATENOMFILE = "updateNomination.php",
            REMOVENOMFILE = "removeNom.php", ADDNOTIFFILE = "addNotification.php";;


    public Nomination(){
        name = "Default";
    }

    public Nomination(Activity act){
        parent = act;
    }

    public void setAttributes(String nm, String desc, ArrayList<String> srcs, String nom, int votes, ArrayList filters1, ArrayList cities1, String date){
        name = nm;
        description = desc; 
        sources = srcs;
        nominator = nom;
        voteCount = votes;
        filters = filters1;
        cities = cities1;
        dateAdded = date;
    }

    public void setNominator(String nominatorName, String nominatorPhone, String nominatorBio) {
        this.nominatorName = nominatorName;
        this.nominatorPhone = nominatorPhone;
        this.nominatorBio = nominatorBio;


        System.out.println("mmm " + this.nominatorName + " " + this.nominatorPhone);
    }


    public void addSource(String url) {
        sources.add(url);
    }

    public void addFilter(String filter) {
        filters.add(filter);
    }

    public void addCity(String city) {
        cities.add(city);
    }



    public void updateVote(String path, PollActivity act){
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();

        keys.add("email");
        keys.add("name");

        vals.add(User.Email);
        vals.add(name);

        pollParent = act;

        final Nomination tempNom = this;

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, path, pollParent), false, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                    pollParent.afterVoteUpdated(tempNom);
                }
                else{
                    Toast.makeText(pollParent, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public String toString() {
        return name + " " + voteCount;
    }











    public void removeNom(Activity act) {
        parent = act;
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
        keys.add("name");
        vals.add(name);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, REMOVENOMFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                    if(User.admin){
                        generateNotification();
                    }
                    goToPoll();
                }
                else{
                    Toast.makeText(parent, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void generateNotification() {
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();

        keys.add("name");
        keys.add("type");
        keys.add("email");

        vals.add(name);
        vals.add("Removal");
        vals.add(nominator);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, ADDNOTIFFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                }
                else{
                    Toast.makeText(parent, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }












    public void addNomination(){
        ArrayList<String> keys = getKeys(), vals = getVals();

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, ADDNOMFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                    goToPoll();
                }
                else{
                    Toast.makeText(parent, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void updateNomination(String oldName) {
        ArrayList<String> keys = getKeys(), vals = getVals();
        keys.add("oldName");
        vals.add(oldName);

        Database db = new Database();
        db.update(new RetrievalData(keys, vals, UPDATENOMFILE, parent), true, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                if(result.equals("true")){
                    goToPoll();
                }
                else{
                    Toast.makeText(parent, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToPoll() {
        Intent intent = new Intent(parent, PollActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        parent.startActivity(intent);
        parent.finish();
    }

    private ArrayList<String> getVals() {
        ArrayList<String> vals = new ArrayList<>();

        vals.add(name);
        vals.add(description);
        vals.add(nominator);
        vals.add(dateAdded);

        vals.add(sources.size()+"");
        vals.addAll(sources);
        vals.add(filters.size()+"");
        vals.addAll(filters);
        vals.add(cities.size()+"");
        vals.addAll(cities);

        return vals;
    }

    private ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();

        keys.add("name");
        keys.add("desc");
        keys.add("email");
        keys.add("date");

        keys.add("sourceNo");
        keys.addAll(getKeyForArray("source", sources.size()));
        keys.add("filterNo");
        keys.addAll(getKeyForArray("filter", filters.size()));
        keys.add("cityNo");
        keys.addAll(getKeyForArray("city", cities.size()));

        return keys;
    }


    private ArrayList<String> getKeyForArray(String key, int length){
        ArrayList<String> arrKeys = new ArrayList<>();

        for(int i=1; i<=length; i++){
            arrKeys.add(key+i);
        }

        return arrKeys;
    }







    protected Nomination(Parcel in) {
        name = in.readString();
        description = in.readString();
        nominator = in.readString();
        sources = in.createStringArrayList();
        voteCount = in.readInt();
        filters = in.createStringArrayList();
        cities = in.createStringArrayList();
        dateAdded = in.readString();
        nominatorName = in.readString();
        nominatorPhone = in.readString();
        nominatorBio = in.readString();
    }

    public static final Creator<Nomination> CREATOR = new Creator<Nomination>() {
        @Override
        public Nomination createFromParcel(Parcel in) {
            return new Nomination(in);
        }

        @Override
        public Nomination[] newArray(int size) {
            return new Nomination[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(nominator);
        parcel.writeStringList(sources);
        parcel.writeInt(voteCount);
        parcel.writeStringList(filters);
        parcel.writeStringList(cities);
        parcel.writeString(dateAdded);
        parcel.writeString(nominatorName);
        parcel.writeString(nominatorPhone);
        parcel.writeString(nominatorBio);
    }

}
