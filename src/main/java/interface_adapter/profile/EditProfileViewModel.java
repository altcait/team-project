package interface_adapter.profile;

import interface_adapter.ViewModel;
import use_case.profile.ProfileOutputBoundary;

public class EditProfileViewModel extends ViewModel<EditProfileState> {

    public EditProfileViewModel() {
        super("edit-profile");
        setState(new EditProfileState());
    }
}
