import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {apiDomain} from '../configurations/api.configuration';
import {GenresResponse} from '../models/responses/genres.response';
import {GenreRequest} from '../models/requests/genre.request';
import {GenreResponse} from '../models/responses/genre.response';

@Injectable({
    providedIn: 'root'
})
export class GenreService {

    constructor(private readonly _http: HttpClient) {
    }

    getAll = (): Observable<GenresResponse> =>
        this._http
            .get<GenresResponse>(`${apiDomain}/genres`);

    add = (path: string, genreAddRequest: GenreRequest): Observable<GenreResponse> =>
        this._http
            .post<GenreResponse>(path, genreAddRequest);

    update = (path: string, genreUpdateRequest: GenreRequest): Observable<GenreResponse> =>
        this._http
            .put<GenreResponse>(path, genreUpdateRequest);

    delete = (path: string): Observable<HttpResponse<Object>> =>
        this._http.delete(path, {observe: 'response'});

    deleteUnused = (path: string): Observable<HttpResponse<Object>> =>
        this._http.delete(path, {observe: 'response'});
}