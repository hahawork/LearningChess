package com.uni.learningchess;

import android.os.Environment;

import java.io.File;

interface interf_general {
    String RESOURCE_SDCARD_PATH = Environment.getExternalStorageDirectory()+ File.pathSeparator+"LearningChess";
}
