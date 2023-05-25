import {Injectable} from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpClient
} from '@angular/common/http';
import {from, lastValueFrom, Observable, take} from 'rxjs';
import {JwtResponse} from '../models/responses/jwt.response';
import {apiDomain} from '../configurations/api.configuration';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private readonly _http: HttpClient) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        request = request.clone({
           withCredentials: true
        });

        if (this.isWhiteListed(request))
            return next.handle(request);

        return from(this.setJwt(request, next));
    }

    private setJwt = async (request: HttpRequest<unknown>, next: HttpHandler): Promise<HttpEvent<any>> => {
        const jwt = await this.getJwt();

        if (jwt) {
            request = request.clone({
                setHeaders: {Authorization: `Bearer ${jwt}`}
            });
        }

        return await lastValueFrom(next.handle(request));
    }

    private getJwt = (): Promise<string | undefined> =>
        new Promise((resolve, reject) => {
            this._http
                .get<JwtResponse>(`${apiDomain}/auth/token`)
                .pipe(take(1))
                .subscribe({
                    next: (response) => resolve(response.token),
                    error: _ => reject(undefined)
                });
        });

    private isWhiteListed = (request: HttpRequest<unknown>): boolean =>
        /auth\/(?!current\b)[\w\/-]+/.test(request.url)
}