import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ErrorComponent} from './features/errors/error/error.component';
import {HomeComponent} from './features/pages/home/home.component';
import {NotFoundComponent} from './features/errors/not-found/not-found.component';
import {ForbiddenComponent} from './features/errors/forbidden/forbidden.component';
import {NavigationComponent} from './features/components/navigation/navigation.component';
import {NavigationAsideComponent} from './features/components/navigation-aside/navigation-aside.component';
import {HttpClientModule} from '@angular/common/http';
import {SecondsToTimePipe} from './core/pipes/seconds-to-time.pipe';
import {RegistrationComponent} from './features/pages/registration/registration.component';
import {LoginComponent} from './features/pages/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FooterComponent} from './features/components/footer/footer.component';
import {interceptors} from './core/configurations/interceptors.configuration';
import {GenresComponent} from './features/pages/genres/genres.component';
import {DialogWarningComponent} from './features/components/dialogs/dialog-warning/dialog-warning.component';
import {DialogAddGenreComponent} from './features/components/dialogs/dialog-add-genre/dialog-add-genre.component';
import {
    DialogUpdateGenreComponent
} from './features/components/dialogs/dialog-update-genre/dialog-update-genre.component';
import {ProfileViewComponent} from './features/pages/profile/profile-view/profile-view.component';
import {ProfileUpdateComponent} from './features/pages/profile/profile-update/profile-update.component';
import {SearchPipe} from './core/pipes/search.pipe';
import {MyAlbumsComponent} from './features/pages/my-albums/my-albums.component';
import {AlbumComponent} from './features/components/album/album.component';
import {AlbumDetailsComponent} from './features/pages/album-details/album-details.component';
import {DialogAddMelodyComponent} from './features/components/dialogs/dialog-add-melody/dialog-add-melody.component';
import {DialogEditAlbumComponent} from './features/components/dialogs/dialog-edit-album/dialog-edit-album.component';
import {DiscoveryComponent} from './features/pages/discovery/discovery.component';
import {ArtistsComponent} from './features/pages/artists/artists.component';

@NgModule({
    declarations: [
        AppComponent,
        ErrorComponent,
        HomeComponent,
        NotFoundComponent,
        ForbiddenComponent,
        NavigationComponent,
        NavigationAsideComponent,
        SecondsToTimePipe,
        RegistrationComponent,
        LoginComponent,
        FooterComponent,
        GenresComponent,
        DialogWarningComponent,
        DialogAddGenreComponent,
        DialogUpdateGenreComponent,
        ProfileViewComponent,
        ProfileUpdateComponent,
        SearchPipe,
        MyAlbumsComponent,
        AlbumComponent,
        AlbumDetailsComponent,
        DialogAddMelodyComponent,
        DialogEditAlbumComponent,
        DiscoveryComponent,
        ArtistsComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        FormsModule
    ],
    providers: [interceptors],
    bootstrap: [AppComponent]
})
export class AppModule {
}