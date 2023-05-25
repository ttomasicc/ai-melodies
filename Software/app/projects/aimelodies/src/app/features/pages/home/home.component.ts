import {Component, OnDestroy, OnInit} from '@angular/core';
import {firstValueFrom, Subscription, take, tap} from 'rxjs';
import {MelodiesPageableResponse} from '../../../core/models/responses/melodies-pageable.response';
import {MelodyService} from '../../../core/services/melody.service';
import * as audio from '../../../core/modules/audio';
import {MelodyResponse} from '../../../core/models/responses/melody.response';
import {AlbumResponse} from '../../../core/models/responses/album.response';
import {AlbumService} from '../../../core/services/album.service';
import {ArtistService} from '../../../core/services/artist.service';
import {Melody} from '../../../core/models/domain/melody';
import {Album} from '../../../core/models/domain/album';
import {AlbumsPageableResponse} from '../../../core/models/responses/albums-pageable.response';
import {Artist} from '../../../core/models/domain/artist';
import {Router} from '@angular/router';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.sass']
})
export class HomeComponent implements OnInit {
    latestMelodies: Melody[] = [];
    latestAlbums: Album[] = [];
    currentArtist?: Artist;

    constructor(
        private readonly _router: Router,
        private readonly _melodyService: MelodyService,
        private readonly _albumService: AlbumService,
        private readonly _artistService: ArtistService
    ) {
    }

    async ngOnInit(): Promise<void> {
        this.currentArtist = await this.getCurrentArtist();
        await this.getLatestAlbums();
        await this.getLatestMelodies();
    }

    navigateToAlbum = async (albumHref: string) => {
        await this._router.navigate(
            ['/albums'],
            {state: {albumHref}}
        );
    }

    navigateToArtist = async (artistHref: string, event: any) => {
        event.stopPropagation();
        await this._router.navigate(
            ['/artists'],
            {state: {artistHref}}
        );
    }

    private getLatestMelodies = (): Promise<void> =>
        new Promise((resolve, reject) => {
            this._melodyService.getLatest()
                .pipe(take(1))
                .subscribe({
                    next: async (response: MelodiesPageableResponse) => {
                        let melodies: Melody[] = response._embedded?.melodies.map(
                            (it: MelodyResponse) => it as Melody
                        ) ?? [];
                        melodies = await this.addMelodyDuration(melodies);
                        melodies = await this.addMelodyArtist(melodies);
                        this.latestMelodies = melodies;
                        resolve();
                    },
                    error: (err) => reject(err)
                });
        });


    private getLatestAlbums = (): Promise<void> =>
        new Promise((resolve, reject) => {
            this._albumService.getLatest()
                .pipe(take(1))
                .subscribe({
                    next: async (response: AlbumsPageableResponse) => {
                        let albums: Album[] = response._embedded?.albums.map(
                            (it: AlbumResponse) => it as Album
                        ) ?? [];
                        albums = await this.addAlbumArtist(albums);
                        this.latestAlbums = albums;
                        resolve();
                    },
                    error: (err) => reject(err)
                })
        });

    private addMelodyDuration = async (melodies: Melody[]): Promise<Melody[]> =>
        await Promise.all(
            melodies.map(async (melody) => {
                const audioDuration = await audio.load(melody.audio);
                return {
                    ...melody,
                    duration: Math.trunc(audioDuration.duration)
                };
            })
        );

    private addMelodyArtist = async (melodies: Melody[]): Promise<Melody[]> => {
        for (const melody of melodies)
            melody.artist = await firstValueFrom(this._artistService.get(melody._links.artist.href))
        return melodies;
    }

    private addAlbumArtist = async (albums: Album[]): Promise<Album[]> => {
        for (const album of albums)
            album.artist = await firstValueFrom(this._artistService.get(album._links.artist.href))
        return albums;
    }

    private getCurrentArtist = (): Promise<Artist | undefined> =>
        new Promise((resolve) => {
            this._artistService
                .fetchCurrent()
                .then((artist) => resolve(artist))
                .catch((err) => resolve(err));
        });
}