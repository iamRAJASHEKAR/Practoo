package com.example.wave.practoo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SampleActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    TextView view, view2;
    int japam, old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbie);
        view = findViewById(R.id.text1);
        view2 = findViewById(R.id.text2);
        japam = 150;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                japam = japam - 1;
                view2.setText(String.valueOf(japam));

            }
        });


        disposable.add(getNotesObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new io.reactivex.functions.Function<Note, Note>() {
                    @Override
                    public Note apply(Note note) throws Exception {
                        // Making the note to all uppercase
                        note.setNote(note.getNote().toUpperCase());
                        return note;
                    }
                }).subscribeWith(getNotesObserver()));
    }

    private DisposableObserver<Note> getNotesObserver() {
        return new DisposableObserver<Note>() {

            @Override
            public void onNext(Note note) {
                Log.d(TAG, "Note: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All notes are emitted!");
            }
        };
    }

    private io.reactivex.Observable<Note> getNotesObservable() {
        final List<Note> notes = prepareNotes();
        return io.reactivex.Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Note> prepareNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "buy tooth paste!"));
        notes.add(new Note(2, "call brother!"));
        notes.add(new Note(3, "watch narcos tonight!"));
        notes.add(new Note(4, "pay power bill!"));
        return notes;
    }

    class Note {
        int id;
        String note;

        public Note(int ids, String notes)
        {
            this.id = ids;
            this.note = notes;
        }

        public int getId() {
            return id;
        }

        public String getNote() {
            return note;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause()

    {
        super.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            japam = japam + 1;
            view2.setText(String.valueOf(japam));
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            japam = japam - 1;
            view2.setText(String.valueOf(japam));

        }
        return true;
    }
}
