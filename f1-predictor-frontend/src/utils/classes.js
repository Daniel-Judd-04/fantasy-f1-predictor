export class Sorter {
    constructor(sortOptions, sortDesc) {
        this.sortOptions = sortOptions;
        this.sortDesc = sortDesc;
    }
}

export class LoadingMessage {
    constructor(stage, maxStage, title, message, success) {
        this.stage = stage;
        this.maxStage = maxStage;
        this.title = title;
        this.message = message;
        this.success = success;
    }
}

export class OverlayData {
    constructor(overlayObject, overlayArray, overlayType) {
        this.overlayObject = overlayObject;
        this.overlayArray = overlayArray;
        this.overlayType = overlayType;
    }
}

// export class Constructor {
//
//     constructor(constructorId, name, shortName, color, drivers) {
//         this.constructorId = constructorId;
//         this.name = name;
//         this.shortName = shortName;
//         this.color = color;
//         this.drivers = drivers;
//     }
// }
//
// export class Driver {
//     constructor(driverId, name, points, fantasyPoints, fantasyPrice, constructorId) {
//         this.driverId = driverId;
//         this.name = name;
//         this.points = points;
//         this.fantasyPoints = fantasyPoints;
//         this.fantasyPrice = fantasyPrice;
//         this.constructorId = constructorId;
//     }
// }