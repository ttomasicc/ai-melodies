import {Injectable} from '@angular/core';
import {apiDomain, apiMelodies} from '../configurations/api.configuration';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {MelodiesPageableResponse} from '../models/responses/melodies-pageable.response';
import {map, Observable} from 'rxjs';
import {MelodiesResponse} from '../models/responses/melodies.response';
import {MelodyResponse} from '../models/responses/melody.response';
import * as httpUtils from '../modules/http';
import {Resource} from '../models/domain/Resource';
import {SearchRequest} from '../models/requests/search.request';

@Injectable({
    providedIn: 'root'
})
export class MelodyService {

    constructor(private readonly _http: HttpClient) {
    }

    getAll = (url: string): Observable<MelodiesResponse> =>
        this._http
            .get<MelodiesResponse>(url)
            .pipe(
                map((melodiesResponse) => {
                    melodiesResponse._embedded?.melodies.forEach((melody) =>
                        melody.audio = `${apiMelodies}/${melody.audio}`
                    );
                    return melodiesResponse;
                })
            );

    getLatest = (): Observable<MelodiesPageableResponse> => {
        const url = `${apiDomain}/info/new?resourceType=melody`;

        return this._http
            .get<MelodiesPageableResponse>(url)
            .pipe(
                map((pageable) => {
                    pageable._embedded?.melodies?.forEach((melody) =>
                        melody.audio = `${apiMelodies}/${melody.audio}`
                    );
                    return pageable;
                })
            );
    }

    search = (request: SearchRequest): Observable<MelodiesPageableResponse> => {
        const url = request.url ? request.url : `${apiDomain}/info/search?${httpUtils.toQueryParams({
            title: request.searchQuery,
            page: 0,
            resourceType: Resource.MELODY
        })}`;

        return this._http
            .get<MelodiesPageableResponse>(url)
            .pipe(
                map((pageable) => {
                    pageable._embedded?.melodies?.forEach((melody) =>
                        melody.audio = `${apiMelodies}/${melody.audio}`
                    );
                    return pageable;
                })
            );
    }

    add = (url: string, melodyAddRequest: FormData): Observable<MelodyResponse> =>
        this._http
            .post<MelodyResponse>(url, melodyAddRequest)
            .pipe(
                map((melody) => {
                    melody.audio = `${apiMelodies}/${melody.audio}`
                    return melody;
                })
            );

    delete = (url: string): Observable<HttpResponse<any>> =>
        this._http
            .delete(url, {observe: 'response'});
}