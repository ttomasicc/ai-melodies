import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {JwtInterceptor} from '../interceptors/jwt.interceptor';
import {CachingInterceptor} from '../interceptors/caching.interceptor';

export const interceptors = [
    {provide: HTTP_INTERCEPTORS, useClass: CachingInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
];