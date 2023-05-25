import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {inject} from '@angular/core';
import {ArtistService} from '../services/artist.service';
import {Observable} from 'rxjs';
import {Role} from '../models/domain/role';
import {Artist} from '../models/domain/artist';

export const canActivate: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree => {
    const authService = inject(ArtistService);
    const router = inject(Router);

    const role: Role = route.data['ROLE'];
    const forbidden: UrlTree = router.createUrlTree(['/error/forbidden']);

    if (role)
        return authService.fetchCurrent()
            .then((artist) => authenticate(artist, role) ? true : forbidden)
            .catch(_ => forbidden)

    return true;
}

function authenticate(artist: Artist, role: Role): boolean {
    if (role === Role.ADMINISTRATOR) {
        return artist.role === Role.ADMINISTRATOR;
    } else if (role === Role.ARTIST)
        return artist.role !== Role.PUBLIC;

    return true;
}