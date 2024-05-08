

public class EnemyUndead extends ObjectEnemy{

    private double timerStrikeTick = 0,                    
                   timerStrikeAttackDelay = .35 * handler.GAME_HERTZ,
                   timerStrikeTotalDuration =  timerStrikeAttackDelay + .15 * handler.GAME_HERTZ,
                   angleTarget = 0, xIntercept = 0, yIntercept =0,
                   strikeAttackMoveSpeed = 3;
    private boolean isAttack = false;


    

    public EnemyUndead(GameObjectHandler gameObjectHandler, double x, double y) {
        super(gameObjectHandler, x, y, 3, new ImageLoader().loadImage("res/coll_circle_32x32.png"));
        this.healthMax = 20;
        this.health = this.healthMax;
        characterSprite = new ImageLoader().loadImage("res/Undead Sprite.png");
    }

    public void attack() {
        
        if(state != ObjectState.attacking && isNearViaCenter(handler.player.centerX, handler.player.centerY, handler.gameUnit * 1.5)){
            state = ObjectState.attacking;
            angleTarget = getDegrees(centerX, centerY, handler.player.centerX, handler.player.centerY);
            velX=0;
            velY=0;
        }
            
        if(state == ObjectState.attacking){
            timerStrikeTick++;

            if(!isAttack && timerStrikeTick >= timerStrikeAttackDelay){
                double  seconds = (timerStrikeTotalDuration - timerStrikeAttackDelay) / handler.GAME_HERTZ,
                        attackOffset = handler.gameUnit*1.6; //distance between object and attack.      
                xIntercept = Math.cos(degreesToRadians(angleTarget));
                yIntercept = Math.sin(degreesToRadians(angleTarget));

                handler.addToGame(new AttackEnemyUndead(this,
                                                    centerX+xIntercept*attackOffset, 
                                                    centerY+yIntercept*attackOffset,                                                    
                                                    seconds,
                                                    angleTarget));                
                angleTarget = 0;
                isAttack = true; 
            }

            if(isAttack){
                //movement due to attack
                velX = xIntercept * strikeAttackMoveSpeed;
                velY = yIntercept * strikeAttackMoveSpeed;
            }

            if(timerStrikeTick >= timerStrikeTotalDuration){                
                state = ObjectState.idle;
                isAttack = false;
                timerStrikeTick=0;
                x = floor(x);
                y = floor(y);
            }
        }        
    }
    
}
