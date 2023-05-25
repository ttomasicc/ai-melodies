import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AlbumService} from '../../../core/services/album.service';
import {ArtistService} from '../../../core/services/artist.service';
import {Artist} from '../../../core/models/domain/artist';
import {lastValueFrom} from 'rxjs';
import {Album} from '../../../core/models/domain/album';

@Component({
    selector: 'app-artists',
    templateUrl: './artists.component.html',
    styleUrls: ['./artists.component.sass']
})
export class ArtistsComponent implements OnInit {
    artist?: Artist;
    albums: Album[] = [];

    private readonly artistHref: string = '';

    constructor(
        private readonly _router: Router,
        private readonly _artistService: ArtistService,
        private readonly _albumService: AlbumService
    ) {
        const state = this._router.getCurrentNavigation()?.extras.state;
        if (state) this.artistHref = state['artistHref'];
    }

    async ngOnInit(): Promise<void> {
        this.artist = await this.getArtist();
        this.albums = await this.getAlbums(this.artist);
    }

    private getArtist = async (): Promise<Artist> =>
        await lastValueFrom(this._artistService.get(this.artistHref));

    private getAlbums = async (artist: Artist): Promise<Album[]> => {
        const response = await lastValueFrom(this._albumService.getAll(artist._links.albums.href));
        return this.addAlbumArtist(response._embedded?.albums ?? []);
    }

    private addAlbumArtist = (albums: Album[]): Album[] =>
        albums.map((album) => {
            album.artist = this.artist;
            return album;
        });
}