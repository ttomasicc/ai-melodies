import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse
} from '@angular/common/http';
import {Observable, of, tap} from 'rxjs';
import {CachingService} from '../services/caching.service';

@Injectable()
export class CachingInterceptor implements HttpInterceptor {

    constructor(private readonly _cachingService: CachingService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (request.method !== 'GET' || this.isJwtRequest(request))
            return next.handle(request);

        const cachedResponse = this._cachingService.get(request.urlWithParams);
        if (cachedResponse) return of(cachedResponse);

        return next.handle(request)
            .pipe(
                tap((response) => {
                    if (response instanceof HttpResponse) {
                        this._cachingService.set(request.urlWithParams, response);
                    }
                })
            );
    }

    private isJwtRequest = (request: HttpRequest<unknown>): boolean =>
        /token/i.test(request.url);
}