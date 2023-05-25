import {Component, OnDestroy, OnInit} from '@angular/core';
import {Artist} from '../../../core/models/domain/artist';
import {Subscription} from 'rxjs';
import {ArtistService} from '../../../core/services/artist.service';
import {Role} from '../../../core/models/domain/role';

@Component({
    selector: 'app-navigation-aside',
    templateUrl: './navigation-aside.component.html',
    styleUrls: ['./navigation-aside.component.sass']
})
export class NavigationAsideComponent implements OnInit, OnDestroy {
    currentArtist?: Artist;
    RoleType = Role;

    private _sub?: Subscription

    constructor(private readonly _artistService: ArtistService) {
    }

    ngOnInit(): void {
        this._sub = this._artistService.getCurrent()
            .subscribe({
                next: (artist) => this.currentArtist = artist
            });
    }

    ngOnDestroy(): void {
        this._sub?.unsubscribe();
    }
}