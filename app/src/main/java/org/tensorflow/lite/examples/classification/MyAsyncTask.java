package org.tensorflow.lite.examples.classification;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    final int MAX = 7;
    int value;
    ProgressBar progressBar;

    public MyAsyncTask(int value, ProgressBar progressbar)
    {
        this.progressBar = progressbar;
        this.value = value;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setMax(MAX);
        progressBar.setProgress(0);
    }

    @Override
    protected Boolean doInBackground(Void... strings){
        for(int i=0; i<= value; i++)
        {
            publishProgress(i);
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0].intValue());
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Boolean s) {
        super.onCancelled(s);
    }
}