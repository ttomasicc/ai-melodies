import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {GenreService} from '../../../../core/services/genre.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Album} from '../../../../core/models/domain/album';
import {AlbumService} from '../../../../core/services/album.service';

@Component({
  selector: 'app-dialog-edit-album[album]',
  templateUrl: './dialog-edit-album.component.html',
  styleUrls: ['./dialog-edit-album.component.sass']
})
export class DialogEditAlbumComponent implements OnInit, OnChanges {
    @Input('album') album!: Album;
    @Input('open') dialogOpen: boolean = false;

    @Output('success') successEmitter: EventEmitter<Album> = new EventEmitter<Album>();
    @Output('close') closeEmitter: EventEmitter<void> = new EventEmitter<void>();

    albumFormGroup!: FormGroup;
    saveButtonPressed: boolean = false;
    errorMessage = '';

    private fileEvent?: any;

    constructor(
        private readonly _fb: FormBuilder,
        private readonly _albumService: AlbumService,
        private readonly _genreService: GenreService
    ) {
    }

    async ngOnInit(): Promise<void> {
        this.buildForm();
    }

    /**
     * Clears form on dialog close action
     * @param changes
     */
    ngOnChanges(changes: SimpleChanges): void {
        if (changes['dialogOpen']                   // exists
            && !changes['dialogOpen'].currentValue  // is closing
            && !changes['dialogOpen'].firstChange   // is not first change
        ) {
            if (this.fileEvent) this.fileEvent.target.value = null;
            this.saveButtonPressed = false;
        }
    }

    onFileChange = (event: any) => {
        this.fileEvent = event;
        const [file] = event.target.files;

        this.albumFormGroup.patchValue({
            image: file ?? null
        });
    }

    updateAlbum = (): void => {
        this.saveButtonPressed = true;
        this.errorMessage = '';

        const request = this.buildRequest();
        if (request) {
            this._albumService
                .update(this.album._links.self.href, request)
                .subscribe({
                    next: (album) => this.successEmitter.emit(album),
                    error: (err: HttpErrorResponse) => {
                        this.saveButtonPressed = false;
                        this.setErrorMessage(err.error.message);
                    }
                });
        } else {
            this.closeEmitter.emit();
        }
    }

    closeDialog = (): boolean => {
        this.closeEmitter.emit();
        this.errorMessage = '';
        return false;
    }

    isNotValid = (field: string): boolean => {
        const formField = this.albumFormGroup.get(field);
        if (formField == undefined) return false;
        return formField.invalid && (formField.dirty || formField.touched);
    }

    private buildRequest = (): FormData | undefined => {
        const formData: FormData = new FormData();

        for (const field in this.albumFormGroup.controls) {
            if (this.validateFormField(field))
                formData.append(field, this.albumFormGroup?.get(field)?.value);
        }

        return (formData as any).entries().next().done ? undefined : formData;
    }

    private validateFormField = (fieldName: string): boolean =>
        this.albumFormGroup.get(fieldName)?.value
        && this.albumFormGroup.get(fieldName)?.value !== this.album[fieldName as keyof typeof this.album]

    private buildForm = (): void => {
        this.albumFormGroup = this._fb.group({
            title: [this.album.title, Validators.compose([
                Validators.maxLength(100)
            ])],
            description: [this.album.description ?? ''],
            image: [null],
        });
    }

    private setErrorMessage = (message: string): void => {
        this.errorMessage = message;
        setTimeout(() => {
            this.errorMessage = '';
        }, 5000);
    }
}