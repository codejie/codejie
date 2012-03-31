
INSERT INTO Boxes VALUES(1, "ground", 3, 0, 3, 790, 10, 0, 1.0, 0.0, 0.0, 16, -1, -1);
INSERT INTO Boxes VALUES(2, "left", 3, 0, 3, 10, 470, 0, 1.0, 0.0, 0.0, 1, -1, -1);
INSERT INTO Boxes VALUES(3, "right", 3, 0, 3, 790, 470, 0, 1.0, 0.0, 0.0, 1, -1, -1);
INSERT INTO Boxes VALUES(4, "floor", 3, 0, 3, 790, 83, 0, 1.0, 0.0, 0.0, 16, -1, -1);
INSERT INTO Boxes VALUES(5, "deadline", 4, 0, 3, 790, -20, 0, 1.0, 0.0, 0.0, 1, -1, -1);
INSERT INTO Boxes VALUES(6, "dock", 2, 2, 0, 450, 72, 0, 1.0, 0.0, 0.9, 17, 4, -1);


INSERT INTO StageBox VALUES(1, 1, 10, 10, -1);
INSERT INTO StageBox VALUES(1, 2, 10, 10, -1);
INSERT INTO StageBox VALUES(1, 3, 790, 10, -1);
INSERT INTO StageBox VALUES(1, 4, 10, 83, -1);
INSERT INTO StageBox VALUES(1, 5, 0, -20, -1);
INSERT INTO StageBox VALUES(1, 6, 90, 10, -1);

delete from Boxes where idx=6;

INSERT INTO Boxes VALUES(7, "circle", 1, 2, 1, 128, 128, 0, 1.0, 0.0, 0.1, 1, 1, -1);
INSERT INTO Boxes VALUES(8, "box", 1, 2, 0, 64, 64, 3.1416, 5.0, 0.0, 0.03, 1, 2, -1);
INSERT INTO Boxes VALUES(9, "triangle", 1, 2, 2, 64, 64, 0, 10.0, 0.0, 0.09, 1, -1, -1);


INSERT INTO StageBox VALUES(1, 7, 350, 350, 0);
INSERT INTO StageBox VALUES(1, 8, 200, 200, 0);
INSERT INTO StageBox VALUES(1, 9, 450, 450, 3);

delete from Boxes where idx > 6;
