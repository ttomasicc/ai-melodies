import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'secondsToTime'
})
export class SecondsToTimePipe implements PipeTransform {

    transform(seconds?: number): string {
        const safeSeconds = seconds ?? 0;
        const minutes = Math.trunc(safeSeconds / 60);
        const remainingSeconds = safeSeconds % 60;
        const formattedMinutes = String(minutes).padStart(2, '0');
        const formattedSeconds = String(remainingSeconds).padStart(2, '0');
        return `${formattedMinutes}:${formattedSeconds}`;
    }
}