package com.dreampany.quran.data.model;

import com.dreampany.frame.data.model.Base;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Created by Roman-372 on 5/7/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@IgnoreExtraProperties
public class Ayah extends Base {

    private int number;
    private String text;
    private int numberInSurah;
    private int juz;
    private int manzil;
    private int page;
    private int ruku;
    private int hizbQuarter;
    private boolean sajda;
}
