package com.usa.dev.drawerlayout.API;

import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Author: Philippe Volpe
 * Date: 14/01/2016
 * Purpose: Parse error
 */

public class ErrorUtils {

    public static ErrorHandler parseError(Response<?> response, Retrofit retrofit) {

        if (response == null || response.errorBody() == null) {
            Log.d("ErrorUtils", "parseError: error body is null.");
            return null;
        }
        Converter<ResponseBody, ErrorHandler> converter = retrofit.responseConverter(ErrorHandler.class, new Annotation[0]);

        ErrorHandler error;

        try {
            error = converter.convert(response.errorBody());
            Log.e("Error", error.getError().getMessage() + error.getError().getErrorCode() + error.getError().getErrorSubCode());
        } catch (IOException ioe) {
            return new ErrorHandler();
        }
        return error;
    }
}
