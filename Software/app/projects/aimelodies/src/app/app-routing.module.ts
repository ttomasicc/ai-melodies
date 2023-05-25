import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent} from './features/errors/error/error.component';
import {NotFoundComponent} from './features/errors/not-found/not-found.component';
import {ForbiddenComponent} from './features/errors/forbidden/forbidden.component';
import {HomeComponent} from './features/pages/home/home.component';
import {RegistrationComponent} from './features/pages/registration/registration.component';
import {LoginComponent} from './features/pages/login/login.component';
import {GenresComponent} from './features/pages/genres/genres.component';
import {Role} from './core/models/domain/role';
import {canActivate as AuthGuard} from './core/guards/auth.guard';
import {ProfileViewComponent} from './features/pages/profile/profile-view/profile-view.component';
import {ProfileUpdateComponent} from './features/pages/profile/profile-update/profile-update.component';
import {MyAlbumsComponent} from './features/pages/my-albums/my-albums.component';
import {AlbumDetailsComponent} from './features/pages/album-details/album-details.component';
import {DiscoveryComponent} from './features/pages/discovery/discovery.component';
import {ArtistsComponent} from './features/pages/artists/artists.component';

const routes: Routes = [
    {
        path: 'home',
        component: HomeComponent
    },
    {
        path: 'register',
        component: RegistrationComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'profile',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: ProfileViewComponent
    },
    {
        path: 'profile/update',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: ProfileUpdateComponent
    },
    {
        path: 'discover',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: DiscoveryComponent
    },
    {
        path: 'my-albums',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: MyAlbumsComponent
    },
    {
        path: 'albums',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: AlbumDetailsComponent
    },
    {
        path: 'artists',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ARTIST
        },
        component: ArtistsComponent
    },
    {
        path: 'genres',
        canActivate: [AuthGuard],
        data: {
            ROLE: Role.ADMINISTRATOR
        },
        component: GenresComponent
    },
    {
        path: 'error',
        component: ErrorComponent,
        children: [
            {
                path: 'notfound',
                component: NotFoundComponent
            },
            {
                path: 'forbidden',
                component: ForbiddenComponent
            },
            {path: '', redirectTo: 'notfound', pathMatch: 'full'}
        ]
    },
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: '**', component: NotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}