package gravit.code.domain.problemProgress.service;

import gravit.code.domain.problem.dto.request.ProblemResult;
import gravit.code.domain.problemProgress.domain.ProblemProgress;
import gravit.code.domain.problemProgress.domain.ProblemProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemProgressService {

    private final ProblemProgressRepository problemProgressRepository;

    public void saveProblemResults(Long userId, List<ProblemResult> problemResults){
        List<ProblemProgress> problemProgresses = problemResults.stream()
                .map(problemResult ->
                        ProblemProgress.create(problemResult.isCorrect(), problemResult.incorrectCounts(), userId, problemResult.problemId())
                ).toList();

        problemProgressRepository.saveAll(problemProgresses);
    }
}
