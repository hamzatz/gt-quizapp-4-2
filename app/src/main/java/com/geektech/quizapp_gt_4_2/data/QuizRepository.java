package com.geektech.quizapp_gt_4_2.data;

import androidx.lifecycle.LiveData;

import com.geektech.quizapp_gt_4_2.data.history.IHistoryStorage;
import com.geektech.quizapp_gt_4_2.data.remote.IQuizApiClient;
import com.geektech.quizapp_gt_4_2.model.History;
import com.geektech.quizapp_gt_4_2.model.Question;
import com.geektech.quizapp_gt_4_2.model.QuizResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizRepository implements IHistoryStorage, IQuizApiClient {

    private IQuizApiClient quizApiClient;
    private IHistoryStorage historyStorage;

    public QuizRepository(
            IQuizApiClient client,
            IHistoryStorage storage
    ) {
        quizApiClient = client;
        historyStorage = storage;
    }

    private Question shuffleAnswers(Question question) {
        ArrayList<String> answers = new ArrayList<>();

        answers.add(question.getCorrectAnswer());
        answers.addAll(question.getIncorrectAnswers());

        Collections.shuffle(answers);
        question.setAnswers(answers);

        return question;
    }

    @Override
    public void getQuestions(final IQuizApiClient.QuestionsCallback callback) {
        quizApiClient.getQuestions(new IQuizApiClient.QuestionsCallback() {
            @Override
            public void onSuccess(List<Question> result) {
                for (int i = 0; i < result.size(); i++) {
                    result.set(i, shuffleAnswers(result.get(i)));
                }
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    @Override
    public QuizResult getQuizResult(int id) {
        return null;
    }

    @Override
    public int saveQuizResult(QuizResult quizResult) {
        return 0;
    }

    @Override
    public LiveData<List<QuizResult>> getAll() {
        return null;
    }

    @Override
    public LiveData<List<History>> getAllHistory() {
        return historyStorage.getAllHistory();
    }

    @Override
    public void delete(int id) {
        historyStorage.delete(id);
    }

    @Override
    public void deleteAll() {

    }
}
