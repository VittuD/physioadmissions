package org.example;

import java.util.List;

/**
 * This class accepts a List<Student> and List<College>, and allows
 * for the Student-Optimal Deferred Acceptance and College-Optimal
 * Deferred Acceptance algorithms to be executed on the inputs
 *
 * @author Michael Levet
 * @date 01/10/2016
 */
public class MatchMaker {

    private final List<Student> students;
    private final List<College> colleges;

    public MatchMaker(List<Student> students, List<College> colleges){
        this.students = students;
        this.colleges = colleges;
    }

    public void sodaMakeMatches(){
        boolean newProposalMade;

        do{
            newProposalMade = false;
            for(Student s : students){
                if(s.canMakeProposal()){
                    newProposalMade = s.makeProposals();
                }
            }
        }while(newProposalMade);
    }

    public void codaMakeMatches(){
        boolean madeMatch;

        do{
            madeMatch = false;

            for(College c : colleges){
                if(c.canMakeProposal()){
                    madeMatch = c.makeProposals();
                }
            }
        }while(madeMatch);
    }
}
