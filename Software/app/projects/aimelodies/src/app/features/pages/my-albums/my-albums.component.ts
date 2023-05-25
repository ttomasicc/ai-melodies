import {Component, OnInit} from '@angular/core';
import {Album} from '../../../core/models/domain/album';
import {AlbumService} from '../../../core/services/album.service';
import {Artist} from '../../../core/models/domain/artist';
import {ArtistService} from '../../../core/services/artist.service';
import {take} from 'rxjs';

@Component({
    selector: 'app-my-albums',
    templateUrl: './my-albums.component.html',
    styleUrls: ['./my-albums.component.sass']
})
export class MyAlbumsComponent implements OnInit {
    albums: Album[] = [];
    searchTerm: string = '';

    private _artist!: Artist;

    constructor(
        private readonly _artistService: ArtistService,
        private readonly _albumService: AlbumService
    ) {
    }

    async ngOnInit(): Promise<void> {
        this._artist = await this.setCurrentArtist();
        this.albums = (await this.getAllAlbums()).sort((a, b) =>
            new Date(b.dateCreated).getTime() - new Date(a.dateCreated).getTime()
        );
    }

    addAlbum = (): void => {
        this._albumService
            .add(this._artist._links.albums.href)
            .pipe(take(1))
            .subscribe({
                next: (albumResponse) => {
                    const album = albumResponse as Album;
                    album.artist = this._artist;
                    this.albums.unshift(album);
                }
            });
    }

    clearSearch = (): void => {
        this.searchTerm = '';
    }

    private getAllAlbums = async (): Promise<Album[]> =>
        new Promise((resolve, reject) => {
            this._albumService
                .getAll(this._artist?._links.albums.href ?? '')
                .pipe(take(1))
                .subscribe({
                    next: (albumsResponse) => {
                        let albums: Album[] = [];

                        if (albumsResponse._embedded) {
                            albums = albumsResponse._embedded.albums;
                            albums.forEach((album) => album.artist = this._artist);
                        }

                        resolve(albums);
                    },
                    error: (err) => reject(err)
                });
        });

    private setCurrentArtist = async (): Promise<Artist> =>
        new Promise((resolve, reject) => {
            this._artistService
                .getCurrent()
                .pipe(take(1))
                .subscribe({
                    next: (artist) => {
                        this._artist = artist!!;
                        resolve(artist!!);
                    },
                    error: (err) => reject(err)
                });
        });
}