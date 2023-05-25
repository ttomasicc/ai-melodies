import {Component, OnDestroy, OnInit} from '@angular/core';
import {ArtistService} from '../../../core/services/artist.service';
import {Artist} from '../../../core/models/domain/artist';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.sass']
})
export class NavigationComponent implements OnInit, OnDestroy {
    currentArtist?: Artist;
    detailsOpen: boolean = false;

    private _sub?: Subscription

    constructor(
        private readonly _router: Router,
        private readonly _artistService: ArtistService
    ) {
    }

    ngOnInit(): void {
        this._sub = this._artistService.getCurrent()
            .subscribe({
                next: (artist) => this.currentArtist = artist
            })
    }

    profile = async (): Promise<void> => {
        await this._router.navigate(['/profile']);
    }

    logout = async (): Promise<void> => {
        await this._artistService.logout();
        await this._router.navigate(['/']);
    }

    clickDetails = (event: any): void => {
        event.preventDefault();
        this.detailsOpen = !this.detailsOpen;
    }

    ngOnDestroy(): void {
        this._sub?.unsubscribe();
    }
}