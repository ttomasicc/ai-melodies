import {Component} from '@angular/core';
import {AlbumService} from '../../../core/services/album.service';
import {MelodyService} from '../../../core/services/melody.service';
import {Album} from '../../../core/models/domain/album';
import {Melody} from '../../../core/models/domain/melody';
import {Resource} from '../../../core/models/domain/Resource';
import {Pageable} from '../../../core/models/responses/pageable';
import {AlbumsPageableResponse} from '../../../core/models/responses/albums-pageable.response';
import {MelodiesPageableResponse} from '../../../core/models/responses/melodies-pageable.response';
import {firstValueFrom, take} from 'rxjs';
import {SearchRequest} from '../../../core/models/requests/search.request';
import {ArtistService} from '../../../core/services/artist.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-discovery',
    templateUrl: './discovery.component.html',
    styleUrls: ['./discovery.component.sass']
})
export class DiscoveryComponent {
    searchTerm: string = '';
    searchPlaceholder: string = '';
    currentPage!: Pageable;
    albums: Album[] = [];
    melodies: Melody[] = [];
    ResourceType = Resource;
    message: string = '';
    loadingResults: boolean = false;

    get searchResource(): Resource {
        return this._searchResource;
    }

    set searchResource(resource: Resource) {
        this.message = '';
        this.clearSearch();
        this.clearPagination();

        switch (resource) {
            case Resource.ALBUM:
                this._searchResource = Resource.ALBUM;
                this.searchPlaceholder = 'Search album by title...';
                break;
            case Resource.MELODY:
                this._searchResource = Resource.MELODY;
                this.searchPlaceholder = 'Search melody by name...';
                break;
        }
    }

    private set albumsPageableResponse(response: AlbumsPageableResponse) {
        this._albumsPageableResponse = response;
        this.currentPage = response.page;
    }

    private set melodiesPageableResponse(response: MelodiesPageableResponse) {
        this._melodiesPageableResponse = response;
        this.currentPage = response.page;
    }

    private _searchResource: Resource = Resource.ALBUM;
    private _delayTimer?: NodeJS.Timeout;
    private _albumsPageableResponse?: AlbumsPageableResponse;
    private _melodiesPageableResponse?: MelodiesPageableResponse;

    constructor(
        private readonly _router: Router,
        private readonly _albumService: AlbumService,
        private readonly _melodyService: MelodyService,
        private readonly _artistService: ArtistService
    ) {
        this.searchResource = Resource.ALBUM;
    }

    first = (): void => {
        switch (this.searchResource) {
            case Resource.ALBUM:
                if (this._albumsPageableResponse?._links.first?.href)
                    this.loadAlbums({url: this._albumsPageableResponse._links.first.href});
                break;
            case Resource.MELODY:
                if (this._melodiesPageableResponse?._links.first?.href)
                    this.loadMelodies({url: this._melodiesPageableResponse._links.first.href});
                break;
        }
    }

    previous = (): void => {
        const regex = /page=(\d+)/;
        let href: string = '';

        switch (this.searchResource) {
            case Resource.ALBUM:
                href = this._albumsPageableResponse?._links.self.href ?? '';

                if (href) {
                    const match = href?.match(regex);
                    if (match) {
                        const page = parseInt(match[1]);
                        if (page !== 0)
                            this.loadAlbums({url: href.replace(regex, `page=${page - 1}`)});
                    }
                }
                break;
            case Resource.MELODY:
                href = this._melodiesPageableResponse?._links.self.href ?? '';

                if (href) {
                    const match = href?.match(regex);
                    if (match) {
                        const page = parseInt(match[1]);
                        if (page !== 0)
                            this.loadMelodies({url: href.replace(regex, `page=${page - 1}`)});
                    }
                }
                break;
        }
    }

    next = (): void => {
        switch (this.searchResource) {
            case Resource.ALBUM:
                if (this._albumsPageableResponse?._links.next?.href)
                    this.loadAlbums({url: this._albumsPageableResponse._links.next.href});
                break;
            case Resource.MELODY:
                if (this._melodiesPageableResponse?._links.next?.href)
                    this.loadMelodies({url: this._melodiesPageableResponse._links.next.href});
                break;
        }
    }

    last = (): void => {
        switch (this.searchResource) {
            case Resource.ALBUM:
                if (this._albumsPageableResponse?._links.last?.href)
                    this.loadAlbums({url: this._albumsPageableResponse._links.last.href});
                break;
            case Resource.MELODY:
                if (this._melodiesPageableResponse?._links.last?.href)
                    this.loadMelodies({url: this._melodiesPageableResponse._links.last.href});
                break;
        }
    }

    search = (event: any): void => {
        clearTimeout(this._delayTimer);
        let searchQuery: string = event.target.value ?? '';
        this.searchTerm = searchQuery;
        this.loadingResults = searchQuery !== '';
        this.message = '';

        this._delayTimer = setTimeout(() => {
            searchQuery = event.target.value;
            searchQuery ? this.loadResults({searchQuery}) : this.clearSearch();
        }, 700);
    }

    clearSearch = (): void => {
        clearTimeout(this._delayTimer);
        this.loadingResults = false;
        this.searchTerm = '';
        this.albums = [];
        this.melodies = [];
        this.clearPagination();
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

    private clearPagination = (): void => {
        this._albumsPageableResponse = undefined;
        this._melodiesPageableResponse = undefined;
        this.currentPage = {
            size: 10,
            totalElements: 0,
            totalPages: 0,
            number: 0
        };
    }

    private loadResults = (request: SearchRequest): void => {
        switch (this.searchResource) {
            case Resource.ALBUM:
                this.loadAlbums(request);
                break;
            case Resource.MELODY:
                this.loadMelodies(request);
                break;
        }
    }

    private loadAlbums = (request: SearchRequest): void => {
        this.loadingResults = true;

        this._albumService
            .search(request)
            .pipe(take(1))
            .subscribe({
                next: async (response) => {
                    this.albums = await this.addAlbumArtist(response._embedded?.albums ?? []);
                    this.albumsPageableResponse = response;
                    this.loadingResults = false;

                    if (!this.albums.length) this.message = 'No matching albums were found.';
                }
            });
    }

    private loadMelodies = (request: SearchRequest): void => {
        this.loadingResults = true;

        this._melodyService
            .search(request)
            .pipe(take(1))
            .subscribe({
                next: async (response) => {
                    this.melodies = await this.addMelodyArtist(response._embedded?.melodies ?? []);
                    this.melodiesPageableResponse = response;
                    this.loadingResults = false;

                    if (!this.melodies.length) this.message = 'No matching melodies were found.';
                }
            });
    }

    private addAlbumArtist = async (albums: Album[]): Promise<Album[]> => {
        for (const album of albums)
            album.artist = await firstValueFrom(this._artistService.get(album._links.artist.href))
        return albums;
    }

    private addMelodyArtist = async (melodies: Melody[]): Promise<Melody[]> => {
        for (const melody of melodies)
            melody.artist = await firstValueFrom(this._artistService.get(melody._links.artist.href))
        return melodies;
    }
}