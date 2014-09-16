package com.chooblarin.gtwooblarin.ui.fragment
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import com.chooblarin.gtwooblarin.R
import groovy.transform.CompileStatic

@CompileStatic
class TweetDialogFragment extends DialogFragment {

    private static final String ARGS_CALLBACK = "args_callback";

    public static TweetDialogFragment newInstance(Closure callback) {
        TweetDialogFragment instance = new TweetDialogFragment()
        Bundle args = new Bundle()
        args.putSerializable(ARGS_CALLBACK, callback)
        instance.setArguments(args)

        return instance
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Closure callback = arguments.getSerializable(ARGS_CALLBACK) as Closure

        Dialog dialog = new Dialog(activity)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_tweet)

        EditText input = dialog.findViewById(R.id.input_tweet_text) as EditText
        dialog.findViewById(R.id.dialog_button_tweet).onClickListener = {
            String tweetText = input.text as String
            callback.call(tweetText)
            dismiss()
        }
        return dialog
    }
}