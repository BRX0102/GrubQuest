package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by karentafolla on 11/5/16.
 */

public class Monster {

        private String monsterName;
        private String transportation;
        private String weeklyBudget;
        private String doCook;
        private String birthday;
        private String firstChoice;
        private String secondChoice;
        private String thirdChoice;

        public Monster(){

        }
        public Monster(String monsterName) {
            this.monsterName = monsterName;
        }

        public void addMonster(String monsterName){
            this.monsterName = monsterName;
        }

        public void setMonsterInformation(String weeklyBudget, String doCook, String transportation){
            this.weeklyBudget = weeklyBudget;
            this.doCook = doCook;
            this.transportation = transportation;
        }

        public void setName(String monsterName){
            this.monsterName = monsterName;
        }

        public void setBirthday(String birthday){
            this.birthday = birthday;
        }

        public void setCooking(String doCook) {
            this.doCook = doCook;
        }

        public void setWeeklyBudget(String weeklyBudget) {
            this.weeklyBudget = weeklyBudget;
        }

        public void setTransportation(String transportation){
            this.transportation = transportation;
        }

        public void setChoices(String firstChoice, String secondChoice, String thirdChoice){
            this.firstChoice = firstChoice;
            this.secondChoice = secondChoice;
            this.thirdChoice = thirdChoice;
        }

        public String getFirstChoice(){
            return firstChoice;
        }

        public String getSecondChoice(){
            return secondChoice;
        }

        public String getThirdChoice(){
            return thirdChoice;
        }

        public String getName(){
            return monsterName;
        }

        @Override
        public String toString() {
            return "Monster [monster_name =" + monsterName + "]";
        }

}
