import {Injectable} from '@angular/core';
import {apiAlbums, apiDomain} from '../configurations/api.configuration';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {AlbumsPageableResponse} from '../models/responses/albums-pageable.response';
import {AlbumsResponse} from '../models/responses/albums.response';
import {AlbumResponse} from '../models/responses/album.response';
import * as httpUtils from '../modules/http';
import {Resource} from '../models/domain/Resource';
import {SearchRequest} from '../models/requests/search.request';

@Injectable({
    providedIn: 'root'
})
export class AlbumService {

    constructor(private readonly _http: HttpClient) {
    }

    get = (url: string): Observable<AlbumResponse> =>
        this._http
            .get<AlbumResponse>(url)
            .pipe(
                map((albumResponse) => {
                    if (albumResponse.image !== null)
                        albumResponse.image = `${apiAlbums}/${albumResponse.image}`;
                    return albumResponse;
                })
            );

    getAll = (url: string): Observable<AlbumsResponse> =>
        this._http
            .get<AlbumsResponse>(url)
            .pipe(
                map((albumsResponse) => {
                    albumsResponse._embedded?.albums.forEach((album) => {
                        if (album.image !== null)
                            album.image = `${apiAlbums}/${album.image}`;
                    });
                    return albumsResponse;
                })
            );

    getLatest = (): Observable<AlbumsPageableResponse> => {
        const url = `${apiDomain}/info/new?resourceType=album`;

        return this._http
            .get<AlbumsPageableResponse>(url)
            .pipe(
                map((pageable) => {
                    pageable._embedded?.albums?.forEach((album) => {
                        if (album.image !== null)
                            album.image = `${apiAlbums}/${album.image}`;
                    });
                    return pageable;
                })
            );
    }

    search = (request: SearchRequest): Observable<AlbumsPageableResponse> => {
        const url = request.url ? request.url : `${apiDomain}/info/search?${httpUtils.toQueryParams({
            title: request.searchQuery,
            page: 0,
            resourceType: Resource.ALBUM
        })}`;

        return this._http
            .get<AlbumsPageableResponse>(url)
            .pipe(
                map((pageable) => {
                    pageable._embedded?.albums?.forEach((album) => {
                        if (album.image !== null)
                            album.image = `${apiAlbums}/${album.image}`;
                    });
                    return pageable;
                })
            );
    }

    add = (url: string): Observable<AlbumResponse> =>
        this._http
            .post<AlbumResponse>(url, {})
            .pipe(
                map((album) => {
                    if (album.image !== null)
                        album.image = `${apiAlbums}/${album.image}`;
                    return album;
                })
            );

    update = (url: string, updateAlbumRequest: FormData): Observable<AlbumResponse> =>
        this._http
            .put<AlbumResponse>(url, updateAlbumRequest)
            .pipe(
                map((album) => {
                    if (album.image !== null)
                        album.image = `${apiAlbums}/${album.image}`;
                    return album;
                })
            );

    delete = (url: string): Observable<HttpResponse<any>> =>
        this._http
            .delete(url, {observe: 'response'});
}