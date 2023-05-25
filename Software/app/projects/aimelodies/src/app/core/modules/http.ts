export const toQueryParams = (object: any): string =>
    Object.keys(object)
        .filter(inx => object[inx] !== undefined)
        .map(inx => `${encodeURIComponent(inx)}=${encodeURIComponent(object[inx])}`)
        .join('&');