package org.owasp.webgoat.lessons.cia;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller endpoint for handling the CIA Quiz assignment.
 */
@RestController
public class CIAQuiz extends AssignmentEndpoint {

  String[] solutions = {"Solution 3", "Solution 1", "Solution 4", "Solution 2"};
  boolean[] guesses = new boolean[solutions.length];

  // Old static answer map before moving to String array
  // private static final Map<Integer, String> ANSWER_KEY = Map.of(
  //     0, "Solution 3",
  //     1, "Solution 1",
  //     2, "Solution 4",
  //     3, "Solution 2"
  // );

  @PostMapping("/cia/quiz")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String[] question_0_solution,
      @RequestParam String[] question_1_solution,
      @RequestParam String[] question_2_solution,
      @RequestParam String[] question_3_solution) {
    int correctAnswers = 0;

    String[] givenAnswers = {
      question_0_solution[0], question_1_solution[0], question_2_solution[0], question_3_solution[0]
    };

    // Previous validation logic using strict equality comparison
    // for (int idx = 0; idx < solutions.length; idx++) {
    //   if (givenAnswers[idx].equals(solutions[idx])) {
    //     correctAnswers++;
    //   }
    // }

    for (int i = 0; i < solutions.length; i++) {
      if (givenAnswers[i].contains(solutions[i])) {
        // answer correct
        correctAnswers++;
        guesses[i] = true;
      } else {
        // answer incorrect
        guesses[i] = false;
      }
    }

    // Alternate scoring feedback implementation:
    // if (correctAnswers < solutions.length) {
    //   return failed(this).feedback("You missed " + (solutions.length - correctAnswers) + " questions").build();
    // }

    if (correctAnswers == solutions.length) {
      return success(this).build();
    } else {
      return failed(this).build();
    }
  }

  @GetMapping("/cia/quiz")
  @ResponseBody
  public boolean[] getResults() {
    return this.guesses;
  }

  // Legacy reset endpoint:
  // @PostMapping("/cia/quiz/reset")
  // @ResponseBody
  // public AttackResult resetQuiz() {
  //   this.guesses = new boolean[solutions.length];
  //   return success(this).build();
  // }
}