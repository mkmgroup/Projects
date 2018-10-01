package com.example.voices.util;

import com.example.voices.view.stories.UserStoriesFragment;

public interface FrUserStoriesResultListener<T> {
    UserStoriesFragment finish(T resultado);
}
