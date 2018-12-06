package no.semester.oppgave.PermissionFragment;

 /* class that shows what we need access to from the user and why */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import no.semester.oppgave.weather.R;

public class ShowPermissions extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }


    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_permissions, null, false);
            return rootView;
        }
    }

}
