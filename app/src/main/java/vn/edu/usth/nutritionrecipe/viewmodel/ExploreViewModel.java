package vn.edu.usth.nutritionrecipe.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExploreViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExploreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}