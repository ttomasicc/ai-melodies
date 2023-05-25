import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';


@Injectable({
    providedIn: 'root'
})
export class CachingService {
    private readonly cache: Map<string, HttpResponse<any>>;
    private readonly cacheTimeout: number;

    constructor() {
        this.cache = new Map<string, any>();
        this.cacheTimeout = 400;
    }

    get = (urlWithParams: string): HttpResponse<any> | undefined =>
        this.cache.get(urlWithParams);

    set = (urlWithParams: string, response: HttpResponse<any>): void => {
        this.cache.set(urlWithParams, response);
        setTimeout(() => this.cache.delete(urlWithParams), this.cacheTimeout);
    }
}