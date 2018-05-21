/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package History;

import Action.INameable;

/**
 *
 * @author CMQ
 */
public interface ITransformable<T> extends INameable {

    T applyTransform(T value);
}
