import {Injectable} from '@angular/core';
import {apiArtists, apiDomain} from '../configurations/api.configuration';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, map, Observable, take} from 'rxjs';
import {ArtistResponse} from '../models/responses/artist.response';
import {Artist} from '../models/domain/artist';
import {LoginRequest} from '../models/requests/login.request';
import {RegistrationRequest} from '../models/requests/registration.request';

@Injectable({
    providedIn: 'root'
})
export class ArtistService {
    private _currentArtist: Artist | undefined;
    private _currentArtist$ = new BehaviorSubject<Artist | undefined>(undefined);

    constructor(private readonly _http: HttpClient) {
        this.fetchAndUpdateCurrent().then();
    }

    get = (url: string): Observable<ArtistResponse> =>
        this._http
            .get<ArtistResponse>(url)
            .pipe(
                map((artist) => {
                    if (artist.image !== null)
                        artist.image = `${apiArtists}/${artist.image}`;
                    return artist;
                })
            );

    getCurrent = (): Observable<Artist | undefined> =>
        this._currentArtist$.asObservable();

    update = (url: string, artistUpdate: FormData): Observable<HttpResponse<any>> =>
        this._http
            .put(url, artistUpdate, {observe: 'response'});

    login = (loginRequest: LoginRequest): Observable<boolean> =>
        new Observable<boolean>((subscriber) => {
            this._http
                .post<ArtistResponse>(`${apiDomain}/auth/login`, loginRequest)
                .pipe(take(1))
                .subscribe({
                    next: (artist) => {
                        this.setCurrent(artist);
                        subscriber.next(true);
                        subscriber.complete();
                    },
                    error: (err) => {
                        subscriber.error(err);
                        subscriber.complete();
                    }
                })
        });

    register = (registrationRequest: RegistrationRequest): Observable<boolean> =>
        new Observable<boolean>((subscriber) => {
            this._http
                .post<ArtistResponse>(`${apiDomain}/auth/register`, registrationRequest)
                .pipe(take(1))
                .subscribe({
                    next: () => {
                        subscriber.next(true);
                        subscriber.complete();
                    },
                    error: (err) => {
                        subscriber.error(err);
                        subscriber.complete();
                    }
                })
        });

    logout = (): Promise<void> =>
        new Promise((resolve, reject) => {
            const logoutHref = this._currentArtist?._links.logout;

            if (logoutHref !== undefined) {
                this._http
                    .delete(logoutHref.href, {observe: 'response'})
                    .pipe(take(1))
                    .subscribe({
                        next: _ => {
                            this.setCurrent(undefined);
                            resolve();
                        },
                        error: (err) => {
                            this.setCurrent(undefined);
                            reject(err);
                        }
                    });
            } else {
                resolve();
            }
        });

    fetchAndUpdateCurrent = async (): Promise<Artist | undefined> => {
        try {
            const artist = await this.fetchCurrent();
            this.setCurrent(artist);
            return this._currentArtist;
        } catch (err) {
            this.setCurrent(undefined);
            return this._currentArtist;
        }
    }

    fetchCurrent = async (): Promise<Artist> =>
        new Promise((resolve, reject) => {
            this._http
                .get<ArtistResponse>(`${apiDomain}/auth/current`)
                .pipe(take(1))
                .subscribe({
                    next: (artist) => resolve(artist as Artist),
                    error: _ => reject(undefined)
                });
        });

    private setCurrent = (artist?: Artist) => {
        if (artist && artist.image) {
            artist = {
                ...artist,
                image: `${apiArtists}/${artist.image}`
            }
        }

        this._currentArtist = artist;
        this._currentArtist$.next(artist);
    }
}