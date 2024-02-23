package org.example;

import java.util.ArrayList;

/**
 * This class models a Student in the College-Admissions Problem.
 * The Student class is designed to allow instances to propose in the
 * Student-Optimal Deferred Acceptance algorithm, or respond to College
 * proposals in the College-Optimal Deferred Acceptance algorithm.
 *
 * @author Michael Levet
 * @date 01/10/2016
 */
public class Student {

    private final ArrayList<College> preferences;
    private College match;
    private final String name;

    /**
     * @param name The name of this Student
     */
    public Student(String name){
        this.name = name;
        this.preferences = new ArrayList<>();
    }

    /**
     *
     * @param c The College to insert
     * @return true if c was successfully inserted, false if c was present in the preference List
     */
    public boolean insertLeastPreferredCollege(College c){
        if(c == null || this.preferences.contains(c)){
            return false;
        }

        return this.preferences.add(c);
    }

    /**
     *
     * @param c The College to insert
     * @param preferenceRanking The position in the preference List to insert c
     * @return true if c was successfully inserted, false if c was already present in the preference List
     */
    public boolean insertCollege(College c, int preferenceRanking){
        if(c == null || this.preferences.contains(c)){
            return false;
        }

        if(preferenceRanking >= this.preferences.size()){
            return this.preferences.add(c);
        }

        this.preferences.add(preferenceRanking, c);
        return true;
    }

    /**
     * Determines whether this Student is unmatched and has Colleges to which
     * it has not proposed and with which it is willing to match.
     *
     * @return true if this Student can make proposals, false otherwise.
     */
    public boolean canMakeProposal(){
        return this.match == null && !this.preferences.isEmpty();
    }

    /**
     * This method is used in the Student-Optimal Deferred Acceptance algorithm.
     * The Student proceeds by selecting its most preferred College
     * to which it has not already proposed, and makes a proposal.
     * The method terminates when the Student is either matched or
     * runs out of Colleges to which it can propose.
     *
     * @return true if a College accepts a proposal from this Student, false otherwise
     */
    public boolean makeProposals(){
        College temp;

        do{
            temp = this.preferences.remove(0);
            System.out.println(this + " proposed to " + temp);
            if(temp.acceptProposal(this)){
                this.match = temp;
                System.out.println(temp + " accepted the proposal from " + this);
                return true;
            }

            System.out.println(temp + " rejected the proposal from " + this);

        }while(!this.preferences.isEmpty());

        return false;
    }

    /**
     * This method unmatches this Student from its current mate.
     * We use this method in the College-Optimal Deferred Acceptance algorithm.
     */
    public void unmatch(){
        this.match = null;
    }

    /**
     * This method is used in the College-Optimal Deferred Acceptance algorithm,
     * allowing the Student to process a College's proposal. The Student can
     * accept the College's proposal, unmatching its current mate if necessary;
     * or reject c's proposal.
     *
     * @param c The College proposing to this Student
     * @return true if this Student accepts c's proposal, false otherwise
     */
    public boolean acceptProposal(College c){
        if(!this.preferences.contains(c)){
            return false;
        }

        if(this.match == null){
            this.match = c;
            return true;
        }

        int index = this.preferences.indexOf(c);
        int matchIndex = this.preferences.indexOf((this.match));

        if(index < matchIndex){
            this.match.unmatchStudent(this);
            this.match = c;
            return true;
        }

        return false;
    }

    /**
     * @return College this Student's current mate
     */
    public College getMatch(){
        return this.match;
    }

    /**
     * @return String A String representation of this Student
     */
    public String toString(){
        return "Student " + this.name;
    }

    public String getName() {
        return name;
    }
}
