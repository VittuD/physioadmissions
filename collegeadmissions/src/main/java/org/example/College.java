package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class models a College in the College-Admissions Problem.
 * The College class is designed to allow instances to propose in the
 * College-Optimal Deferred Acceptance algorithm, or respond to Student
 * proposals in the Student-Optimal Deferred Acceptance algorithm.
 *
 * @author Michael Levet
 * @date 01/10/2016
 */
public class College {

    private final PriorityQueue<Student> matches;
    private final ArrayList<Student> preferences;
    private final String name;
    private final int capacity;

    /**
     * @param name The name of this College
     * @param capacity The number of Students this College can admit
     */
    public College(String name, int capacity){
        this.name = name;
        this.preferences = new ArrayList<>();
        this.capacity = capacity;

        //ranks Students based on their positions in the preferences List
        //this Comparator ensures that the PriorityQueue is stores Students
        //in order of increasing preference
        Comparator<Student> ranking = (s1, s2) -> {
            int indexOne = College.this.preferences.indexOf(s1);
            int indexTwo = College.this.preferences.indexOf(s2);

            return indexTwo - indexOne;
        };

        this.matches = new PriorityQueue<>(capacity, ranking);

    }

    /**
     *
     * @param s The Student to insert
     * @return true if s was successfully inserted, false if s was present in the preference List
     */
    public boolean insertLeastPreferredStudent(Student s){
        if(s == null || this.preferences.contains(s)){
            return false;
        }

        return this.preferences.add(s);
    }

    /**
     *
     * @param s The Student to insert
     * @param preferenceRanking The order in the preferences List to insert s
     * @return true if s was successfully inserted, false if s was present in the preference List
     */
    public boolean insertStudent(Student s, int preferenceRanking){
        if(s == null || this.preferences.contains(s)){
            return false;
        }

        if(preferenceRanking >= this.preferences.size()){
            return this.preferences.add(s);
        }

        this.preferences.add(preferenceRanking, s);
        return true;
    }

    /**
     * This method is used in the College-Optimal Deferred Acceptance algorithm.
     * A College can make proposals if it has not reached its quota of admitted Students
     * and it has Students to which it has not proposed (and with which it is willing to match).
     *
     * @return true if this College can make proposals, false otherwise
     */
    public boolean canMakeProposal(){
        return this.matches.size() < this.capacity && !this.preferences.isEmpty();
    }

    /**
     * @param s The Student to remove from this College's admitted students
     */
    public void unmatchStudent(Student s){
        System.out.println(this + " unmatched from " + s);
        this.matches.remove(s);
    }

    /**
     * This method is used in the College-Optimal Deferred Acceptance algorithm.
     * This College proposes to Students in preference order until it has fulfilled
     * its quota or runs out of Students to which it can propose.
     *
     * @return true if this College added a Student to its matches, false otherwise
     */
    public boolean makeProposals(){
        boolean madeMatch = false;

        while(!this.preferences.isEmpty() && this.matches.size() < this.capacity){
            Student temp = this.preferences.remove(0);
            System.out.println(this + " proposed to " + temp);

            if(temp.acceptProposal(this)){
                this.matches.add(temp);
                madeMatch = true;
                System.out.println(temp + " accepted proposal from " + temp);
                continue;
            }

            System.out.println(temp + " rejected proposal from " + temp);

        }

        return true;
    }

    /**
     * This method is used in the Student-Optimal Deferred Acceptance algorithm,
     * allowing the College to process a Student's proposal. The College may accept
     * the proposal, unmatching its current mate if necessary; or reject the proposal.
     *
     * @param other The Student proposing to this College
     * @return true iff this College accepted the proposal
     */
    public boolean acceptProposal(Student other){
        int index = this.preferences.indexOf(other);
        if(index == -1){
            return false;
        }

        if(this.matches.size() == this.capacity){
            int indexOther = this.preferences.indexOf(this.matches.peek());
            if(index < indexOther){
                Student revoked = this.matches.poll();
                System.out.println(this + " unmatched from " + revoked);
                if (revoked != null) {
                    revoked.unmatch();
                }
                this.matches.add(other);
                return true;
            }

            return false;
        }

        this.matches.add(other);
        return true;
    }

    /**
     * @return PriorityQueue<Student> The admitted Students for this College
     */
    public PriorityQueue<Student> getMatches(){
        return this.matches;
    }

    /**
     * @return A String representation of this College
     */
    public String toString(){
        return "College " + this.name;
    }

    public String getName() {
        return name;
    }
}
